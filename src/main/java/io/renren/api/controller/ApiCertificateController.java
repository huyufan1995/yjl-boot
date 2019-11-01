package io.renren.api.controller;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import cn.hutool.core.lang.Validator;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.CertificateEntity;
import io.renren.cms.service.CertificateService;
import io.renren.enums.AuditStatusEnum;
import io.renren.utils.annotation.TokenMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 认证
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Api("实名认证")
@RestController
@RequestMapping("/api/certificate")
public class ApiCertificateController {

	@Autowired
	private CertificateService certificateService;

	@PostMapping("/submit")
	@ApiOperation(value = "实名认证【提交认证信息】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "认证类型 个人personage 企业enterprise", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "license", value = "营业执照【企业必填】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "identityCardFront", value = "身份证正面【个人必填】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "identityCardReverse", value = "身份证反面【个人必填】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "姓名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true)
	})
	public ApiResult submit(@ApiIgnore CertificateEntity certificateEntity,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		if (!Validator.isMobile(certificateEntity.getMobile())) {
			return ApiResult.error(500, "手机号格式错误");
		}

		CertificateEntity certificate = certificateService.queryObjectByMemberId(sessionMember.getSuperiorId());
		if (certificate != null && StringUtils.equals(AuditStatusEnum.PENDING.getCode(), certificate.getStatus())) {
			return ApiResult.error(500, "您申请的正在审核，请勿重复提交");
		}
		Date now = new Date();
		if (certificate == null) {
			certificateEntity.setCtime(now);
			certificateEntity.setUtime(now);
			certificateEntity.setIsDel(SystemConstant.F_STR);
			certificateEntity.setMemberId(sessionMember.getSuperiorId());
			certificateEntity.setStatus(AuditStatusEnum.PENDING.getCode());
			certificateService.save(certificateEntity);
		} else {
			certificateEntity.setId(certificate.getId());
			certificateEntity.setUtime(now);
			certificateEntity.setStatus(AuditStatusEnum.PENDING.getCode());
			certificateService.update(certificateEntity);
		}
		return ApiResult.ok();
	}

	@PostMapping("/query_audit")
	@ApiOperation(value = "查询审核状态")
	public ApiResult queryAudit(@ApiIgnore @TokenMember SessionMember sessionMember) {
		CertificateEntity certificate = certificateService.queryObjectByMemberId(sessionMember.getSuperiorId());
		HashMap<String, String> result = Maps.newHashMap();
		if(certificate == null) {
			result.put("status", "notapply");
			result.put("msg", "未申请");
			return ApiResult.ok(result);
		}
		result.put("status", certificate.getStatus());
		if(AuditStatusEnum.PENDING.getCode().equals(certificate.getStatus())) {
			result.put("msg", "审核中");
		}else if(AuditStatusEnum.PASS.getCode().equals(certificate.getStatus())) {
			result.put("msg", "已通过");
		}else if(AuditStatusEnum.REJECT.getCode().equals(certificate.getStatus())) {
			result.put("msg", "不通过");
			result.put("reason", certificate.getReason());
		}
		return ApiResult.ok(result);
	}
}
