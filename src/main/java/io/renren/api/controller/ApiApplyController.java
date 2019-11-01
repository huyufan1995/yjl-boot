package io.renren.api.controller;

import java.io.File;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ApplyRecordVo;
import io.renren.api.vo.ApplyVo;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.TemplateApplyEntity;
import io.renren.cms.entity.TemplateApplyUseRecordEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.cms.service.ApplyService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.TemplateApplyService;
import io.renren.cms.service.TemplateApplyUseRecordService;
import io.renren.cms.service.TemplateService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.MemberRoleEnum;
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
 * 报名模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:45
 */
@RestController
@RequestMapping("/api/apply")
@Api("报名")
@Slf4j
public class ApiApplyController {
	
	@Autowired
	private TemplateApplyService templateApplyService;
	@Autowired
	private ApplyService applyService;
	@Autowired
	private ApplyRecordService applyRecordService;
	@Autowired
	private TemplateApplyUseRecordService templateApplyUseRecordService;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private COSClient cosClient;
	@Autowired
	private MemberService memberService;
	@Autowired
	private TemplateService templateService;
	
	@IgnoreAuth
	@ApiOperation("模板列表【首页显示】")
	@PostMapping("/list_index")
	public ApiResult listIndex() {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("isRelease", SystemConstant.T_STR);
		params.put("page", 1);
		params.put("limit", 12);//首页显示12条
		params.put("sidx", "utime");
		params.put("order", "desc");
		Query query = new Query(params);
		List<TemplateApplyEntity> templateList = templateApplyService.queryList(query);//utime desc
		templateList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getExamplePath()) && !item.getExamplePath().startsWith("http")) {
				item.setExamplePath(yykjProperties.getVisitprefix().concat(item.getExamplePath()));
			}
		});
		return ApiResult.ok(templateList);
	}
	
	@IgnoreAuth
	@ApiOperation("模板列表【全部】")
	@PostMapping("/all")
	public ApiResult all() {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("isRelease", SystemConstant.T_STR);
		List<TemplateApplyEntity> templateList = templateApplyService.queryList(params);
		templateList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getExamplePath()) && !item.getExamplePath().startsWith("http")) {
				item.setExamplePath(yykjProperties.getVisitprefix().concat(item.getExamplePath()));
			}
			item.setUsePortrait(templateApplyUseRecordService.queryListByTemplateApplyId(item.getId()).stream()
					.map(TemplateApplyUseRecordEntity::getAvatarUrl).collect(Collectors.toList()));
		});
		return ApiResult.ok(templateList);
	}
	
	@ApiOperation("发起记录")
	@PostMapping("/creation_record")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult creationRecord(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("superiorId", sessionMember.getSuperiorId());
		params.put("memberId", sessionMember.getMemberId());
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<ApplyEntity> applyList = applyService.queryListCreationRecord(query);
		List<ApplyVo> applyVoList = ProjectUtils.convertApply(applyList, sessionMember);
		return ApiResult.ok(applyVoList);
	}
	
	@ApiOperation("报名线索")
	@PostMapping("/apply_list")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司，dept部门 ", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult applyList(@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "ownerOpenid", required = false) String ownerOpenid,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		
		HashMap<String, Object> params = Maps.newHashMap();
		
		if (StringUtils.equals(type, "company")) {
			//公司
			params.put("superiorId", sessionMember.getSuperiorId());
		} else if (StringUtils.equals(type, "my")) {
			//普通员工
			if (StringUtils.isBlank(ownerOpenid)) {
				params.put("memberId", sessionMember.getMemberId());
			} else {
				MemberEntity memberEntity = memberService.queryObjectByOpenid(ownerOpenid);
				params.put("memberId", memberEntity.getId());
			}
		} else if (StringUtils.equals(type, "dept")) {
			//部门
			params.put("deptId", sessionMember.getDeptId());
		}
		
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<ApplyEntity> applyList = applyService.queryListShareRecord(query);
		HashMap<String, Object> paramsRecord = Maps.newHashMap();
		applyList.forEach(item -> {
			paramsRecord.put("applyId", item.getId());
			paramsRecord.put("shareMemberId", params.get("memberId"));
			item.setRecordCount(applyRecordService.queryTotal(paramsRecord));
		});
		List<ApplyVo> applyVoList = ProjectUtils.convertApply(applyList, sessionMember);
		return ApiResult.ok(applyVoList);
	}
	
	@ApiOperation("报名数据")
	@PostMapping("/apply_data")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司，dept部门 ", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "报名ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "搜索关键字", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult applyData(@RequestParam Integer applyId,
			@RequestParam(value = "key", required = false) String key, 
			@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("page", page);
		params.put("limit", limit);
		params.put("applyId", applyId);
		params.put("key", key);
		List<ApplyRecordEntity> applyRecordList = null;
		if (StringUtils.equals(type, "company")) {
			//公司
			params.put("superiorId", sessionMember.getSuperiorId());
			Query query = new Query(params);
			applyRecordList = applyRecordService.queryListByApplyId(query);//包括分享人姓名
		} else if (StringUtils.equals(type, "my")) {
			//普通员工
			params.put("shareMemberId", sessionMember.getMemberId());
			Query query = new Query(params);
			applyRecordList = applyRecordService.queryList(query);
		} else if (StringUtils.equals(type, "dept")) {
			//部门
			params.put("deptId", sessionMember.getDeptId());
			Query query = new Query(params);
			applyRecordList = applyRecordService.queryList(query);
		}

		applyRecordList.forEach(item -> {
			item.setItemDetailJsonObj(JSON.parseObject(item.getItemDetail()));
		});
		return ApiResult.ok(applyRecordList);
	}
	
	@ApiOperation("报名数据详情")
	@PostMapping("/apply_data_info")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "数据ID", required = true)
	})
	public ApiResult applyDataInfo(@RequestParam Integer id) {
		ApplyRecordEntity applyRecordEntity = applyRecordService.queryObject(id);
		Assert.isNullApi(applyRecordEntity, "该数据不存在");
		return ApiResult.ok(applyRecordEntity.getItemDetail());
	}
	
	@ApiOperation("报名数据删除")
	@PostMapping("/apply_data_delete")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "数据ID", required = true)
	})
	public ApiResult applyDataDelete(@RequestParam Integer id) {
		applyRecordService.logicDel(id);
		return ApiResult.ok();
	}
	
	@ApiOperation("报名删除")
	@PostMapping("/apply_delete")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "报名活动ID", required = true)
	})
	public ApiResult applyDelete(@RequestParam Integer id) {
		applyService.logicDelete(id);
		return ApiResult.ok();
	}
	
	@ApiOperation("编辑报名")
	@PostMapping("/edit_apply")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "报名ID【新建不传，修改传】", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "templateApplyId", value = "报名模板ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "permission", value = "类型 公开public 个人private", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "活动名称", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "describe", value = "活动描述", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "startTime", value = "开始时间", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "endTime", value = "结束时间", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "images", value = "图片集【多张逗号相隔】", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "items", value = "表单项 如【姓名,手机】", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "address", value = "地址", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "addressLongitude", value = "地址经度", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "addressLatitude", value = "地址纬度", required = false)
	})
	public ApiResult editApply(@ApiIgnore @Validated ApplyEntity applyEntity,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		if(applyEntity.getEndTime().before(applyEntity.getStartTime())) {
			return ApiResult.error(500, "截至时间错误");
		}
		ApplyVo applyVo = applyService.editApply(applyEntity, sessionMember);
		return ApiResult.ok(applyVo);
	}
	
	@IgnoreAuth
	@ApiOperation("报名活动详情")
	@PostMapping("/apply_info")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "报名ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "joinOpenid", value = "报名人openid", required = true)
	})
	public ApiResult applyInfo(@RequestParam Integer applyId, @RequestParam String joinOpenid) {
		ApplyEntity applyEntity = applyService.queryObject(applyId);
		Assert.isNullApi(applyEntity, "该报名活动不存在");
		ApplyVo applyVo = new ApplyVo();
		BeanUtil.copyProperties(applyEntity, applyVo);
		TemplateApplyEntity templateApplyEntity = templateApplyService.queryObject(applyVo.getTemplateApplyId());
		Assert.isNullApi(templateApplyEntity, "模板不存在");
		applyVo.setLayout(templateApplyEntity.getLayout());
		applyVo.setExamplePath(templateApplyEntity.getExamplePath());
		ApplyRecordEntity applyRecordEntity = applyRecordService.queryObjectByApplyIdAndJoinOpenid(applyId, joinOpenid);
		if (applyRecordEntity != null) {
			applyVo.setJoin(true);//已参加报名
		}

		//参与人头像5个
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("applyId", applyId);
		Query query = new Query(params);
		List<ApplyRecordEntity> applyRecordList = applyRecordService.queryList(query);
		applyVo.setJoinPortraits(
				applyRecordList.stream().map(ApplyRecordEntity::getJoinPortrait).collect(Collectors.toList()));
		
		//更新报名浏览量
		if(!StringUtils.equals(applyEntity.getOpenid(), joinOpenid)) {
			ApplyEntity temp = new ApplyEntity();
			temp.setId(applyEntity.getId());
			temp.setViewCount(applyEntity.getViewCount() + 1);
			applyService.update(temp);
		}
		
		return ApiResult.ok(applyVo);
	}
	
	@IgnoreAuth
	@ApiOperation("参与报名")
	@PostMapping("/join_apply")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "报名活动ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "joinOpenid", value = "报名人openid", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "itemDetail", value = "提交明细json", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人membearid", required = true)
	})
	public ApiResult joinApply(@ApiIgnore @Validated ApplyRecordEntity applyRecordEntity) {
		applyService.joinApply(applyRecordEntity);
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@ApiOperation("参与记录")
	@PostMapping("/join_record")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult joinRecord(@RequestParam String openid,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("joinOpenid", openid);
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<ApplyRecordVo> applyRecordVoList = applyRecordService.queryListByJoinOpenid(query);
		Date now = new Date();
		for (ApplyRecordVo applyVo : applyRecordVoList) {
			if (applyVo.getStartTime().after(now)) {
				applyVo.setStatus("未开始");
			} else if (applyVo.getStartTime().before(now) && applyVo.getEndTime().after(now)) {
				applyVo.setStatus("进行中");
			} else {
				applyVo.setStatus("已结束");
			}
		}
		return ApiResult.ok(applyRecordVoList);
	}
	
	@IgnoreAuth
	@ApiOperation("参与详情")
	@PostMapping("/join_info")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "applyRecordId", value = "参与记录ID", required = true)
	})
	public ApiResult joinInfo(@RequestParam Integer applyRecordId) {
		ApplyRecordEntity applyRecordEntity = applyRecordService.queryObject(applyRecordId);
		Assert.isNullApi(applyRecordEntity, "参与记录不存在");
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("joinPortrait", applyRecordEntity.getJoinPortrait());
		resultMap.put("joinNickName", applyRecordEntity.getJoinNickName());
		
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("applyId", applyRecordEntity.getApplyId());
		Query query = new Query(params);
		List<ApplyRecordEntity> applyRecordList = applyRecordService.queryList(query);
		resultMap.put("joinPortraits", applyRecordList.stream().map(ApplyRecordEntity::getJoinPortrait).collect(Collectors.toList()));
		
		ApplyEntity applyEntity = applyService.queryObject(applyRecordEntity.getApplyId());
		Assert.isNullApi(applyEntity, "报名活动不存在");
		resultMap.put("applyName", applyEntity.getName());
		resultMap.put("applyStartTime", applyEntity.getStartTime());
		resultMap.put("applyEndTime", applyEntity.getEndTime());
		resultMap.put("applyDescribe", applyEntity.getDescribe());
		return ApiResult.ok(resultMap);
	}
	
	@ApiOperation("查看报名活动小程序码")
	@PostMapping("/view_qrcode")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "报名活动ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人memberid", required = true)
	})
	public ApiResult viewQrcode(@RequestParam Integer applyId, @RequestParam Integer shareMemberId) {
		ApplyEntity applyEntity = applyService.queryObject(applyId);
		Assert.isNullApi(applyEntity, "该报名活动不存在");
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("name", applyEntity.getName());
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_APPLY_DETAIL, applyId, shareMemberId), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			resultMap.put("qrcode", yykjProperties.getImagePrefixUrl().concat(key));
		} catch (WxErrorException e) {
			log.error("===生成报名活动小程序码异常：{}", e.getMessage());
		}
		return ApiResult.ok(resultMap);
	}
	
	@IgnoreAuth
	@ApiOperation("活动分享图片")
	@PostMapping("/view_share_image")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "报名活动ID", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "shareMemberId", value = "分享人memberid", required = true)
	})
	public ApiResult viewShareImage(@RequestParam Integer applyId, @RequestParam Integer shareMemberId) {
		ApplyEntity applyEntity = applyService.queryObject(applyId);
		Assert.isNullApi(applyEntity, "该活动不存在");
		TemplateEntity templateEntity = templateService.queryObject(491);
		Assert.isNullApi(templateEntity, "模板不存在");
		templateEntity.setImageTemplate(yykjProperties.getVisitprefix() + templateEntity.getImageTemplate());
		List<TemplateItmeEntity> templateItmeList = templateService.queryListByTemplateId(491);
		if (CollectionUtil.isEmpty(templateItmeList)) {
			return ApiResult.error(500, "模板参数不存在");
		}
	
		for (TemplateItmeEntity templateItme : templateItmeList) {
			//标题名称
			if (templateItme.getId().equals(2601)) {
				templateItme.setDescribe(applyEntity.getName());
			}
			
			if (templateItme.getId().equals(2602)) {
				templateItme.setDescribe("扫描二维码立即参加");
				
			}
			
			//小程序码
			if (templateItme.getId().equals(2603)) {
				final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
				try {
					File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
							StrUtil.format(SystemConstant.APP_PAGE_PATH_APPLY_DETAIL, applyId, shareMemberId), 280, false, null, false);
					String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
					templateItme.setImagePath(yykjProperties.getImagePrefixUrl().concat(key));
				} catch (WxErrorException e) {
					log.error("===生成报名活动小程序码异常：{}", e.getMessage());
				}
			}
		}

		File generateShareImage = ProjectUtils.generateShareImage(templateItmeList, templateEntity);
		String shareImageUrl = ProjectUtils.uploadCosFile(cosClient, generateShareImage);
		String fullUrl = yykjProperties.getImagePrefixUrl() + shareImageUrl;
		log.info("==============活动分享图片：{}", fullUrl);
		return ApiResult.ok(fullUrl);
	}
}
