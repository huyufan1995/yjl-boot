package io.renren.api.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.CaseVo;
import io.renren.cms.entity.CaseBrowseEntity;
import io.renren.cms.entity.CaseEntity;
import io.renren.cms.entity.CaseInformEntity;
import io.renren.cms.entity.CaseLeaveEntity;
import io.renren.cms.entity.CasePraiseEntity;
import io.renren.cms.entity.CaseShareEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.service.CaseBrowseService;
import io.renren.cms.service.CaseInformService;
import io.renren.cms.service.CaseLeaveService;
import io.renren.cms.service.CasePraiseService;
import io.renren.cms.service.CaseService;
import io.renren.cms.service.CaseShareService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.TemplateService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.MemberRoleEnum;
import io.renren.enums.PermTypeEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.ProjectUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 案例
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@Slf4j
@RestController
@RequestMapping("/api/case")
@Api("案例库")
public class ApiCaseController {

	@Autowired
	private CaseService caseService;
	@Autowired
	private CasePraiseService casePraiseService;
	@Autowired
	private CaseShareService caseShareService;
	@Autowired
	private CaseBrowseService caseBrowseService;
	@Autowired
	private CaseLeaveService caseLeaveService;
	@Autowired
	private CaseInformService caseInformService;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private COSClient cosClient;
	@Autowired
	private MemberService memberService;
	@Autowired
	private TemplateService templateService;
	
	@ApiOperation("全部案例")
	@PostMapping("/all")
	public ApiResult all(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("superiorId", sessionMember.getSuperiorId());
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<CaseVo> caseVoList = caseService.queryListByMemberIdOrSuperiorId(query);
		if (MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())
				&& PermTypeEnum.LEADER.getCode().equals(sessionMember.getPerm())) {
			//主管只能编辑自己的
			caseVoList.forEach(c -> {
				if(c.getMemberId().equals(sessionMember.getMemberId())) {
					c.setEdit(true);
				}
			});
		} else if (MemberRoleEnum.ADMIN.getCode().equals(sessionMember.getRole())
				|| MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {

			//管理员都可编辑
			caseVoList.forEach(c -> {
				c.setEdit(true);
			});
		}
		return ApiResult.ok(caseVoList);
	}
	
	@ApiOperation("我的上传")
	@PostMapping("/my")
	public ApiResult my(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("memberId", sessionMember.getMemberId());
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<CaseVo> caseVoList = caseService.queryListByMemberIdOrSuperiorId(query);
		if (MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())
				&& PermTypeEnum.LEADER.getCode().equals(sessionMember.getPerm())) {
			//主管只能编辑自己的
			caseVoList.forEach(c -> {
				if(c.getMemberId().equals(sessionMember.getMemberId())) {
					c.setEdit(true);
				}
			});
		} else if (MemberRoleEnum.ADMIN.getCode().equals(sessionMember.getRole())
				|| MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {

			//管理员都可编辑
			caseVoList.forEach(c -> {
				c.setEdit(true);
			});
		}
		return ApiResult.ok(caseVoList);
	}
	
	@ApiOperation("我的分享")
	@PostMapping("/my_share")
	public ApiResult myShare(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("memberId", sessionMember.getMemberId());
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<CaseVo> caseVoList = caseService.queryListMyShare(query);
		if (MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())
				&& PermTypeEnum.LEADER.getCode().equals(sessionMember.getPerm())) {
			//主管只能编辑自己的
			caseVoList.forEach(c -> {
				if(c.getMemberId().equals(sessionMember.getMemberId())) {
					c.setEdit(true);
				}
			});
		} else if (MemberRoleEnum.ADMIN.getCode().equals(sessionMember.getRole())
				|| MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {

			//管理员都可编辑
			caseVoList.forEach(c -> {
				c.setEdit(true);
			});
		}
		return ApiResult.ok(caseVoList);
	}
	
	@ApiOperation("案例线索")
	@PostMapping("/case_list")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司, dept部门 ", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult caseList(@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("page", page);
		params.put("limit", limit);

		if (StringUtils.equals("my", type)) {
			//普通员工
			params.put("memberId", sessionMember.getMemberId());
		} else if (StringUtils.equals("company", type)) {
			//公司
			params.put("shareSuperiorId", sessionMember.getSuperiorId());
		} else if (StringUtils.equals("dept", type)) {
			//部门
			params.put("deptId", sessionMember.getDeptId());
		}

		Query query = new Query(params);
		List<CaseVo> caseVoList = caseService.queryListMyShare(query);
		return ApiResult.ok(caseVoList);
	}
	
	@ApiOperation("编辑案例")
	@PostMapping("/edit_case")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "案例ID【新建不传，修改传】", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "title", value = "标题", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "details", value = "详情JSON", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "intro", value = "简介", required = false)
	})
	public ApiResult editCase(@ApiIgnore @Validated CaseEntity caseEntity,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		if (!wxMaService.getSecCheckService().checkMessage(caseEntity.getTitle())) {
			return ApiResult.error(500, "包含敏感词汇");
		}
		if (!wxMaService.getSecCheckService().checkMessage(caseEntity.getDetails())) {
			return ApiResult.error(500, "包含敏感词汇");
		}
		Date now = new Date();
		if (caseEntity.getId() == null) {
			caseEntity.setCtime(now);
			caseEntity.setUtime(now);
			caseEntity.setIsDel(SystemConstant.F_STR);
			caseEntity.setMemberId(sessionMember.getMemberId());
			caseEntity.setSuperiorId(sessionMember.getSuperiorId());
			caseEntity.setViewCnt(0);
			caseService.save(caseEntity);
		} else {
			caseEntity.setUtime(now);
			caseService.update(caseEntity);
		}

		return ApiResult.ok(caseEntity.getId());
	}
	
	@IgnoreAuth
	@ApiOperation("案例详情")
	@PostMapping("/case_info")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "浏览人openid", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人会员ID】【非必填】", required = false)
	})
	public ApiResult caseInfo(@RequestParam Integer caseId, @RequestParam String openid,
			@RequestParam(value = "shareMemberId", required = false) Integer shareMemberId) {
		CaseVo caseVo = caseService.caseInfo(caseId, openid, shareMemberId);
		return ApiResult.ok(caseVo);
	}
	
	@IgnoreAuth
	@ApiOperation("赞案例【点赞/取消点赞】")
	@PostMapping("/case_praise")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true)
	})
	public ApiResult casePraise(@RequestParam Integer caseId, @RequestParam String openid) {
		CasePraiseEntity casePraiseEntity = casePraiseService.queryObjectByOpenidAndCaseId(openid, caseId);
		if (casePraiseEntity == null) {
			casePraiseEntity = new CasePraiseEntity();
			casePraiseEntity.setCaseId(caseId);
			casePraiseEntity.setCtime(new Date());
			casePraiseEntity.setOpenid(openid);
			casePraiseService.save(casePraiseEntity);
		} else {
			casePraiseService.delete(casePraiseEntity.getId());
		}
		return ApiResult.ok();
	}
	
	@ApiOperation("案例分享")
	@PostMapping("/case_share")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人会员ID", required = true)
	})
	public ApiResult caseShare(@RequestParam Integer caseId, @RequestParam Integer shareMemberId) {
		CaseShareEntity caseShareEntity = caseShareService.queryObjectByCaseIdAndShareMemberId(caseId, shareMemberId);
		if (caseShareEntity == null) {
			caseShareEntity = new CaseShareEntity();
			caseShareEntity.setCaseId(caseId);
			caseShareEntity.setShareMemberId(shareMemberId);
			caseShareEntity.setShareTime(new Date());
			
			MemberEntity shareMember = memberService.queryObject(shareMemberId);
			Assert.isNullApi(shareMember, "分享人不存在");
			if(MemberRoleEnum.BOSS.getCode().equals(shareMember.getRole())) {
				shareMember.setSuperiorId(shareMember.getId());
			}
			caseShareEntity.setShareSuperiorId(shareMember.getSuperiorId());
			caseShareService.save(caseShareEntity);
		}
		return ApiResult.ok();
	}
	
	@ApiOperation("案例【访客列表】")
	@PostMapping("/case_data_browse")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司，dept部门", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = false)
	})
	public ApiResult caseDataBrowse(@RequestParam(value = "caseId", required = false) Integer caseId,
			@RequestParam(value = "type", required = false, defaultValue = "my") String type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		HashMap<String, Object> param = Maps.newHashMap();
		param.put("caseId", caseId);
		param.put("page", page);
		param.put("limit", limit);
		
		if (StringUtils.equals("my", type)) {
			param.put("shareMemberId", sessionMember.getMemberId());
		} else if(StringUtils.equals("company", type)) {
			param.put("superiorId", sessionMember.getSuperiorId());
		} else if(StringUtils.equals("dept", type)) {
			param.put("deptId", sessionMember.getDeptId());
		}
		Query query = new Query(param);
		List<CaseBrowseEntity> caseBrowseList = caseBrowseService.queryList(query);
		return ApiResult.ok(caseBrowseList);
	}
	
	@ApiOperation("案例数据【留言列表】")
	@PostMapping("/case_data_leave")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司, dept部门", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = false)
	})
	public ApiResult caseDataLeave(@RequestParam(value = "caseId", required = false) Integer caseId,
			@RequestParam(value = "type", required = false, defaultValue = "my") String type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		HashMap<String, Object> param = Maps.newHashMap();
		param.put("caseId", caseId);
		param.put("page", page);
		param.put("limit", limit);
		
		if (StringUtils.equals("my", type)) {
			param.put("shareMemberId", sessionMember.getMemberId());
		} else if(StringUtils.equals("company", type)){
			param.put("superiorId", sessionMember.getSuperiorId());
		} else if(StringUtils.equals("dept", type)) {
			param.put("deptId", sessionMember.getDeptId());
		}
		Query query = new Query(param);
		List<CaseLeaveEntity> caseLeaveList = caseLeaveService.queryList(query);
		return ApiResult.ok(caseLeaveList);
	}
	

	@ApiOperation("案例数据【留言详情】")
	@PostMapping("/case_data_leave_info")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseLeaveId", value = "案例留言ID", required = true)
	})
	public ApiResult caseDataLeaveInfo(@RequestParam Integer caseLeaveId,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		CaseLeaveEntity caseLeaveEntity = caseLeaveService.queryObject(caseLeaveId);
		Assert.isNullApi(caseLeaveEntity, "该留言不存在");
		if (caseLeaveEntity.getShareMemberId().equals(sessionMember.getMemberId())) {
			if (StringUtils.equals(SystemConstant.F_STR, caseLeaveEntity.getStatus())) {
				caseLeaveEntity.setStatus(SystemConstant.T_STR);//已读
				caseLeaveService.update(caseLeaveEntity);
			}
		}

		CaseEntity caseEntity = caseService.queryObject(caseLeaveEntity.getCaseId());
		Assert.isNullApi(caseEntity, "该案例不存在");
		caseLeaveEntity.setCaseTitle(caseEntity.getTitle());
		MemberEntity shareMember = memberService.queryObject(caseLeaveEntity.getShareMemberId());
		if(shareMember != null) {
			caseLeaveEntity.setShareRealName(shareMember.getRealName());
		}
		
		MemberEntity creationMember = memberService.queryObject(caseEntity.getMemberId());
		if(creationMember != null) {
			caseLeaveEntity.setCreationRealName(creationMember.getRealName());
		}
		
		return ApiResult.ok(caseLeaveEntity);
	}
	
	@ApiOperation("案例数据【留言删除】")
	@PostMapping("/case_data_leave_delete")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseLeaveId", value = "案例留言ID", required = true)
	})
	public ApiResult caseDataLeaveDelete(@RequestParam Integer caseLeaveId) {
		caseLeaveService.logicDel(caseLeaveId);
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@ApiOperation("案例数据【留言提交】")
	@PostMapping("/case_data_leave_submit")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "content", value = "留言内容", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "留言人openid", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "姓名", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人ID", required = true)
	})
	public ApiResult caseDataLeaveSubmit(@ApiIgnore @Validated CaseLeaveEntity caseLeaveEntity) {
		caseLeaveEntity.setCtime(new Date());
		caseLeaveEntity.setIsDel(SystemConstant.F_STR);
		caseLeaveEntity.setStatus(SystemConstant.F_STR);
		caseLeaveService.save(caseLeaveEntity);
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@ApiOperation("案例举报")
	@PostMapping("/case_inform")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "举报人openid", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "举报类型", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true)
	})
	public ApiResult caseInform(@ApiIgnore @Validated CaseInformEntity caseInform) {
		CaseInformEntity caseInformEntity = caseInformService.queryObjectByOpenidAndCaseId(caseInform.getOpenid(), caseInform.getCaseId());
		if(caseInformEntity == null) {
			caseInform.setCtime(new Date());
			caseInformService.save(caseInform);
		} else {
			caseInformEntity.setType(caseInform.getType());
			caseInformEntity.setCtime(new Date());
			caseInformService.update(caseInformEntity);
		}
		return ApiResult.ok();
	}
	
	@ApiOperation("查看案例小程序码")
	@PostMapping("/view_qrcode")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人memberid", required = true)
	})
	public ApiResult viewQrcode(@RequestParam Integer caseId, @RequestParam Integer shareMemberId) {
		CaseEntity caseEntity = caseService.queryObject(caseId);
		Assert.isNullApi(caseEntity, "该案例不存在");
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("title", caseEntity.getTitle());
		resultMap.put("intro", caseEntity.getIntro());
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_CASE_DETAIL, caseId, shareMemberId), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			resultMap.put("qrcode", yykjProperties.getImagePrefixUrl().concat(key));
		} catch (WxErrorException e) {
			log.error("===生成案例小程序码异常：{}", e.getMessage());
		}
		return ApiResult.ok(resultMap);
	}
	
	@IgnoreAuth
	@ApiOperation("案例分享图片")
	@PostMapping("/view_share_image")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人memberid", required = true)
	})
	public ApiResult viewShareImage(@RequestParam Integer caseId, @RequestParam Integer shareMemberId) {
		CaseEntity caseEntity = caseService.queryObject(caseId);
		Assert.isNullApi(caseEntity, "该案例不存在");
		TemplateEntity templateEntity = templateService.queryObject(493);
		Assert.isNullApi(templateEntity, "模板不存在");
		templateEntity.setImageTemplate(yykjProperties.getVisitprefix() + templateEntity.getImageTemplate());
		List<TemplateItmeEntity> templateItmeList = templateService.queryListByTemplateId(493);
		if (CollectionUtil.isEmpty(templateItmeList)) {
			return ApiResult.error(500, "模板参数不存在");
		}

		for (TemplateItmeEntity templateItme : templateItmeList) {
			//标题名称
			if (templateItme.getId().equals(2609)) {
				if(StringUtils.isNotBlank(caseEntity.getTitle())) {
					templateItme.setDescribe(caseEntity.getTitle().replaceAll(SystemConstant.REGEX_CASE_TITLE, "$1∫"));
				}else {
					templateItme.setDescribe("");
				}
			}

			//小程序码
			if (templateItme.getId().equals(2610)) {
				final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
				try {
					File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
							StrUtil.format(SystemConstant.APP_PAGE_PATH_CASE_DETAIL, caseId, shareMemberId), 280, false,
							null, false);
					String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
					templateItme.setImagePath(yykjProperties.getImagePrefixUrl().concat(key));
				} catch (WxErrorException e) {
					log.error("===生成报名活动小程序码异常：{}", e.getMessage());
				}
			}

			//详情
			if (templateItme.getId().equals(2611)) {
				if(StringUtils.isNotBlank(caseEntity.getIntro())) {
					templateItme.setDescribe(caseEntity.getIntro().replaceAll(SystemConstant.REGEX_CASE_INTRO, "$1∫"));
				}else {
					templateItme.setDescribe("");
				}
			}
		}

		File generateShareImage = ProjectUtils.generateShareImage(templateItmeList, templateEntity);
		String shareImageUrl = ProjectUtils.uploadCosFile(cosClient, generateShareImage);
		String fullUrl = yykjProperties.getImagePrefixUrl() + shareImageUrl;
		log.info("==============案例分享图片：{}", fullUrl);
		return ApiResult.ok(fullUrl);
	}

	@ApiOperation("删除案例")
	@PostMapping("/delete")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "caseId", value = "案例ID", required = true)
	})
	public ApiResult delete(@RequestParam Integer caseId) {
		caseService.logicDel(caseId);
		return ApiResult.ok();
	}
}
