package io.renren.api.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.CardVo;
import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.service.CardAccessRecordService;
import io.renren.cms.service.CardService;
import io.renren.cms.service.DeptMemberService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.TemplateService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.MemberRoleEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.ProjectUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 名片
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Api("名片")
@RestController
@RequestMapping("/api/card")
@Slf4j
public class ApiCardController {
	
	@Autowired
	private CardService cardService;
	@Autowired
	private CardAccessRecordService cardAccessRecordService;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private COSClient cosClient;
	@Autowired
	private MemberService memberService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private DeptMemberService deptMemberService;
	
	@IgnoreAuth
	@PostMapping("/details")
	@ApiOperation(value = "名片明细")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true) })
	public ApiResult details(@RequestParam String openId) {
		CardVo cardVo = cardService.info(openId);
		return ApiResult.ok(cardVo);
	}

	@IgnoreAuth
	@PostMapping("/info")
	@ApiOperation(value = "名片详情【根据openid获取名片详情】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true) })
	public ApiResult info(@RequestParam(value = "openid") String openid) {
		CardVo cardVo = cardService.info(openid);
		return ApiResult.ok(cardVo);
	}

	@IgnoreAuth
	@PostMapping("/view")
	@ApiOperation(value = "名片查看【自己查看或者别人查看】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "查看人openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "cardId", value = "名片ID", required = true) })
	public ApiResult view(@RequestParam String openid, @RequestParam Integer cardId) {
		CardVo cardVo = cardService.view(openid, cardId);
		return ApiResult.ok(cardVo);
	}

	@IgnoreAuth
	@PostMapping("/modify")
	@ApiOperation(value = "修改名片")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "portrait", value = "头像", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "姓名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "company", value = "公司", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "position", value = "职位", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "weixin", value = "微信号", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "email", value = "邮箱", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "address", value = "地址", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "addressLongitude", value = "经度", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "addressLatitude", value = "纬度", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "introduce", value = "简介", required = false)

	})
	public ApiResult modify(@ApiIgnore @Validated CardEntity cardEntity) {
		if (!Validator.isMobile(cardEntity.getMobile())) {
			return ApiResult.error(500, "手机号格式错误");
		}
		if (StringUtils.isNotBlank(cardEntity.getEmail()) && !Validator.isEmail(cardEntity.getEmail())) {
			return ApiResult.error(500, "邮箱号格式错误");
		}
		cardService.modify(cardEntity);
		return ApiResult.ok();
	}

	@IgnoreAuth
	@PostMapping("/access_records")
	@ApiOperation(value = "名片访客列表【分页】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司, dept部门", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "today", value = "是否今日数据 t是 f否 默认t", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false) })
	public ApiResult accessRecords(@RequestParam String openid,
			@RequestParam(value = "ownerOpenid", required = false) String ownerOpenid,
			@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "today", required = true, defaultValue = "t") String today,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit
			) {
		HashMap<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(ownerOpenid)) {
			//查看别人
			openid = ownerOpenid;
		}
		
		if (StringUtils.equals(type, "my")) {
			//普通员工
			params.put("cardOpenid", openid);
		} else if (StringUtils.equals(type, "company")) {
			//公司
			MemberEntity memberEntity = memberService.queryObjectByOpenid(openid);
			if (MemberRoleEnum.BOSS.getCode().equals(memberEntity.getRole())) {
				memberEntity.setSuperiorId(memberEntity.getId());
			}
			params.put("superiorId", memberEntity.getSuperiorId());
		} else if (StringUtils.equals(type, "dept")) {
			//部门
			MemberEntity memberEntity = memberService.queryObjectByOpenid(openid);
			DeptMemberEntity deptMemberEntity = deptMemberService.queryObjectByMemberId(memberEntity.getId());
			params.put("deptId", deptMemberEntity.getDeptId());
		}
		
		if(StringUtils.equals(today, SystemConstant.T_STR)) {
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
		}
		
		params.put("page", page);
		params.put("limit", limit);
		params.put("sidx", "access_time");
		params.put("order", "desc");
		Query query = new Query(params);
		List<CardAccessRecordEntity> accessRecords = cardAccessRecordService.queryList(query);
		return ApiResult.ok(accessRecords);
	}

	@ApiIgnore
	@IgnoreAuth
	@PostMapping("/access_record")
	@ApiOperation(value = "名片访问记录")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "cardId", value = "名片ID", required = true) })
	public ApiResult accessRecord(@RequestParam Integer cardId) {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("cardId", cardId);
		params.put("sidx", "access_time");
		params.put("order", "desc");
		params.put("offset", 0);
		params.put("limit", 5);

		List<CardAccessRecordEntity> recordList = cardAccessRecordService.queryList(params);
		int recordCount = cardAccessRecordService.queryTotal(params);

		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("portraits",
				recordList.stream().map(CardAccessRecordEntity::getPortrait).collect(Collectors.toList()));
		resultMap.put("recordCount", recordCount);
		return ApiResult.ok(resultMap);

	}
	
	@IgnoreAuth
	@ApiOperation("名片小程序码")
	@PostMapping("/view_qrcode")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "cardId", value = "名片ID", required = true) })
	public ApiResult viewQrcode(@RequestParam Integer cardId) {
		CardEntity cardEntity = cardService.queryObject(cardId);
		Assert.isNullApi(cardEntity, "该名片不存在");
		MemberEntity memberEntity = memberService.queryObjectByOpenid(cardEntity.getOpenid());
		CardVo cardVo = new CardVo();
		BeanUtil.copyProperties(cardEntity, cardVo);
		if (memberEntity != null) {
			//已经是付费会员
			cardVo.setType(memberEntity.getType());
			cardVo.setCertType(memberEntity.getCertType());
		}
		
		if (StringUtils.isNotBlank(cardEntity.getQrcode())) {
			return ApiResult.ok(cardVo);
		}
		
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_CARD_DETAIL, cardEntity.getId()), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			cardEntity.setQrcode(yykjProperties.getImagePrefixUrl().concat(key));
			cardService.update(cardEntity);
			cardVo.setQrcode(cardEntity.getQrcode());
			return ApiResult.ok(cardVo);
		} catch (WxErrorException e) {
			log.error("===生成名片小程序码异常：{}", e.getMessage());
		}
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@ApiOperation("名片分享图片")
	@PostMapping("/view_share_image")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "cardId", value = "名片ID", required = true) })
	public ApiResult viewShareImage(@RequestParam Integer cardId) {
		CardEntity cardEntity = cardService.queryObject(cardId);
		Assert.isNullApi(cardEntity, "该名片不存在");
		TemplateEntity templateEntity = templateService.queryObject(492);
		Assert.isNullApi(templateEntity, "名片模板不存在");
		templateEntity.setImageTemplate(yykjProperties.getVisitprefix() + templateEntity.getImageTemplate());
		List<TemplateItmeEntity> templateItmeList = templateService.queryListByTemplateId(492);
		if (CollectionUtil.isEmpty(templateItmeList)) {
			return ApiResult.error(500, "名片模板参数不存在");
		}
		for (TemplateItmeEntity templateItme : templateItmeList) {
			//头像
			if (templateItme.getId().equals(2604)) {
				templateItme.setImagePath(cardEntity.getPortrait());
			}
			//名片小程序码
			if (templateItme.getId().equals(2605)) {
				templateItme.setImagePath(cardEntity.getQrcode());
			}
			//姓名
			if (templateItme.getId().equals(2606)) {
				templateItme.setDescribe(cardEntity.getName());
			}
			//公司
			if (templateItme.getId().equals(2607)) {
				templateItme.setDescribe(cardEntity.getCompany());
			}
			if (templateItme.getId().equals(2608)) {
				templateItme.setDescribe("长按识别二维码");
			}
		}

		File generateShareImage = ProjectUtils.generateShareImage(templateItmeList, templateEntity);
		String shareImageUrl = ProjectUtils.uploadCosFile(cosClient, generateShareImage);
		String fullUrl = yykjProperties.getImagePrefixUrl() + shareImageUrl;
		log.info("==============名片分享图片：{}", fullUrl);
		return ApiResult.ok(fullUrl);
	}

}
