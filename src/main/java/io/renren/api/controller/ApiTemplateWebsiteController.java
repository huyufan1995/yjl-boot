package io.renren.api.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.CardVo;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.entity.TemplateWebsiteBrowseEntity;
import io.renren.cms.entity.TemplateWebsiteEntity;
import io.renren.cms.entity.TemplateWebsiteLayoutEntity;
import io.renren.cms.entity.TemplateWebsiteMetadataEntity;
import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import io.renren.cms.service.CardService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.TemplateService;
import io.renren.cms.service.TemplateWebsiteBrowseService;
import io.renren.cms.service.TemplateWebsiteLayoutService;
import io.renren.cms.service.TemplateWebsiteMetadataService;
import io.renren.cms.service.TemplateWebsiteService;
import io.renren.cms.service.TemplateWebsiteUseRecordService;
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
 * 官网模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
@Api("官网")
@RestController
@RequestMapping("/api/templatewebsite")
@Slf4j
public class ApiTemplateWebsiteController {

	@Autowired
	private TemplateWebsiteService templateWebsiteService;
	@Autowired
	private TemplateWebsiteUseRecordService templateWebsiteUseRecordService;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private TemplateWebsiteMetadataService templateWebsiteMetadataService;
	@Autowired
	private TemplateWebsiteLayoutService templateWebsiteLayoutService;
	@Autowired
	private CardService cardService;
	@Autowired
	private TemplateWebsiteBrowseService templateWebsiteBrowseService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private COSClient cosClient;
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
		List<TemplateWebsiteEntity> templateList = templateWebsiteService.queryList(query);
		templateList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getExamplePath()) && !item.getExamplePath().startsWith("http")) {
				item.setExamplePath(yykjProperties.getVisitprefix().concat(item.getExamplePath()));
			}
		});
		return ApiResult.ok(templateList);
	}
	
	@ApiOperation("官网访问记录")
	@PostMapping("/browse_record")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司，dept部门", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "today", value = "是否今日数据 t是 f否 默认t", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult browseRecord(@RequestParam String openid,
			@RequestParam(value = "ownerOpenid", required = false) String ownerOpenid,
			@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "today", required = true, defaultValue = "t") String today,
			@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
			@RequestParam(value = "limit", defaultValue = "5", required = false) Integer limit,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("page", page);
		params.put("limit", limit);
		
		if (StringUtils.equals("my", type)) {
			//普通员工
			if (StringUtils.isNotBlank(ownerOpenid)) {
				params.put("ownerOpenid", ownerOpenid);
			} else {
				params.put("ownerOpenid", sessionMember.getOpenid());
			}
		} else if (StringUtils.equals("company", type)) {
			//公司
			params.put("superiorId", sessionMember.getSuperiorId());
		} else if (StringUtils.equals("dept", type)) {
			//部门
			params.put("deptId", sessionMember.getDeptId());
		}
		
		if(StringUtils.equals(today, SystemConstant.T_STR)) {
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
		}
		Query query = new Query(params);
		List<TemplateWebsiteBrowseEntity> browseList = templateWebsiteBrowseService.queryList(query);
		return ApiResult.ok(browseList);
	}
	
	@IgnoreAuth
	@ApiOperation("模板列表【全部】")
	@PostMapping("/all")
	public ApiResult all() {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("isRelease", SystemConstant.T_STR);
		List<TemplateWebsiteEntity> templateList = templateWebsiteService.queryList(params);
		templateList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getExamplePath()) && !item.getExamplePath().startsWith("http")) {
				item.setExamplePath(yykjProperties.getVisitprefix().concat(item.getExamplePath()));
			}
			item.setUsePortrait(templateWebsiteUseRecordService.queryListByTemplateWebsiteId(item.getId()).stream()
					.map(TemplateWebsiteUseRecordEntity::getAvatarUrl).collect(Collectors.toList()));
		});
		return ApiResult.ok(templateList);
	}

	@IgnoreAuth
	@ApiOperation("使用记录")
	@PostMapping("/use_record")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true) })
	public ApiResult useRecord(@RequestParam String openid) {
		HashMap<String, Object> param = Maps.newHashMap();
		param.put("openid", openid);
		List<TemplateWebsiteUseRecordEntity> templateWebsiteUseRecordList = templateWebsiteUseRecordService.queryList(param);
		templateWebsiteUseRecordList.forEach(item -> {
			if(StringUtils.isNotBlank(item.getTemplateImageExample()) && !item.getTemplateImageExample().startsWith("http")) {
				item.setTemplateImageExample(yykjProperties.getVisitprefix().concat(item.getTemplateImageExample()));
			}
		});
		return ApiResult.ok(templateWebsiteUseRecordList);
	}
	
	@ApiIgnore
	@ApiOperation("保存模板元数据")
	@PostMapping("/save_metadata")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "metadata", value = "元数据JSON", required = true) })
	public ApiResult saveMetadata(@RequestParam String metadata, @ApiIgnore @TokenMember SessionMember sessionMember) {
		if(MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())) {
			return ApiResult.error(500, "无权操作");
		}
		TemplateWebsiteMetadataEntity metadataEntity = templateWebsiteMetadataService
				.queryObjectByMemberId(sessionMember.getSuperiorId());
		if (metadataEntity == null) {
			metadataEntity = new TemplateWebsiteMetadataEntity();
			metadataEntity.setCtime(new Date());
			metadataEntity.setMemberId(sessionMember.getSuperiorId());
			metadataEntity.setMetadata(metadata);
			metadataEntity.setOperator(sessionMember.getMemberId());
			metadataEntity.setUtime(new Date());
			templateWebsiteMetadataService.save(metadataEntity);
		} else {
			metadataEntity.setUtime(new Date());
			metadataEntity.setMetadata(metadata);
			metadataEntity.setOperator(sessionMember.getMemberId());
			templateWebsiteMetadataService.update(metadataEntity);
		}
		return ApiResult.ok();
	}
	
	@ApiOperation("获取模板配置数据")
	@PostMapping("/get_conf")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "模板ID", required = true) })
	public ApiResult getConf(@RequestParam Integer id, @ApiIgnore @TokenMember SessionMember sessionMember) {
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("templateId", id);
		TemplateWebsiteEntity websiteEntity = templateWebsiteService.queryObject(id);
		Assert.isNull(websiteEntity, "该模板不存在");
		resultMap.put("layout", websiteEntity.getLayout());
		TemplateWebsiteLayoutEntity layoutEntity = templateWebsiteLayoutService.queryObjectByMemberId(sessionMember.getSuperiorId());
		if(layoutEntity == null) {
			resultMap.put("metadata", websiteEntity.getMetadata());
		} else {
			resultMap.put("metadata", templateWebsiteMetadataService.queryObject(layoutEntity.getMetadataId()).getMetadata());
		}
		return ApiResult.ok(resultMap);
	}
	
	@ApiOperation("保存模板配置【生成微官网】")
	@PostMapping("/save_conf")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "templateId", value = "模板ID", required = true),
//			@ApiImplicitParam(paramType = "query", dataType = "string", name = "layout", value = "布局json【非必选】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "metadata", value = "元数据json【非必选，管理员修改了传】", required = false)
	})
	public ApiResult saveConf(@RequestParam Integer templateId,
			@RequestParam(value = "layout", required = false, defaultValue = "") String layout,
			@RequestParam(value = "metadata", required = false) String metadata,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		if (!wxMaService.getSecCheckService().checkMessage(metadata)) {
			return ApiResult.error(500, "包含敏感词汇");
		}
		templateWebsiteLayoutService.saveConf(templateId, layout, metadata, sessionMember);
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@ApiOperation("官网详情")
	@PostMapping("/info")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "官网所有人openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "当前浏览人openid", required = true)
	})
	public ApiResult info(@RequestParam String ownerOpenid, @RequestParam String openid) {
		MemberEntity ownerMember = memberService.queryObjectByOpenid(ownerOpenid);
		Assert.isNullApi(ownerMember, "该会员不存在");
		if(MemberRoleEnum.BOSS.getCode().equals(ownerMember.getRole())) {
			ownerMember.setSuperiorId(ownerMember.getId());
		}
		if (ownerMember.getEndTime() != null && ownerMember.getEndTime().before(new Date())) {
			return ApiResult.error(10003, "该会员已到期");
		}
		TemplateWebsiteLayoutEntity layoutEntity = templateWebsiteLayoutService.queryObjectByOpenid(ownerOpenid);
		if(layoutEntity == null) {
			//查询上级元数据
			TemplateWebsiteMetadataEntity metadataEntity = templateWebsiteMetadataService.queryObjectByMemberId(ownerMember.getSuperiorId());
			if(metadataEntity == null) {
				metadataEntity = new TemplateWebsiteMetadataEntity();
				metadataEntity.setCtime(new Date());
				metadataEntity.setUtime(new Date());
				metadataEntity.setMetadata(SystemConstant.DEFAULT_METADATA);
				metadataEntity.setMemberId(ownerMember.getSuperiorId());
				metadataEntity.setOperator(ownerMember.getId());
				templateWebsiteMetadataService.save(metadataEntity);
			}
			
			layoutEntity = new TemplateWebsiteLayoutEntity();
			layoutEntity.setCtime(new Date());
			layoutEntity.setUtime(new Date());
			layoutEntity.setTemplateId(1);//默认模板ID
			layoutEntity.setMetadataId(metadataEntity.getId());
			layoutEntity.setMemberId(ownerMember.getId());
			layoutEntity.setOpenid(ownerMember.getOpenid());
			
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			try {
				File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
						StrUtil.format(SystemConstant.APP_PAGE_PATH_WEBSITE_DETAIL, ownerMember.getOpenid()), 280, false, null, false);
				String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
				layoutEntity.setQrcode(yykjProperties.getImagePrefixUrl().concat(key));
			} catch (WxErrorException e) {
				log.error("===生成官网小程序码异常：{}", e.getMessage());
			}
			templateWebsiteLayoutService.save(layoutEntity);
		}
		
		TemplateWebsiteEntity websiteEntity = templateWebsiteService.queryObject(layoutEntity.getTemplateId());
		Assert.isNullApi(websiteEntity, "模板不存在");
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("templateId", layoutEntity.getTemplateId());
		resultMap.put("memberId", layoutEntity.getMemberId());
		resultMap.put("metadata", templateWebsiteMetadataService.queryObject(layoutEntity.getMetadataId()).getMetadata());
		resultMap.put("layout", websiteEntity.getLayout());
		resultMap.put("qrcode", layoutEntity.getQrcode());
		
		CardVo ownerCardVo = cardService.info(ownerOpenid);
		resultMap.put("card", ownerCardVo);
		CardVo browseCardVo = cardService.info(openid);
		
		if (!StringUtils.equals(ownerOpenid, openid)) {
			//非自己浏览 浏览记录
			TemplateWebsiteBrowseEntity browseEntity = templateWebsiteBrowseService
					.queryObjectByOwnerOpenidAndBrowseOpenid(ownerOpenid, openid);
			if (browseEntity == null) {
				browseEntity = new TemplateWebsiteBrowseEntity();
				browseEntity.setAccessTime(new Date());
				browseEntity.setOwnerOpenid(ownerOpenid);
				browseEntity.setBrowseOpenid(openid);
				browseEntity.setTemplateWebsiteId(layoutEntity.getTemplateId());
				browseEntity.setCtime(new Date());
				browseEntity.setCompany(browseCardVo.getCompany());
				browseEntity.setPortrait(browseCardVo.getPortrait());
				browseEntity.setName(browseCardVo.getName());
				browseEntity.setPosition(browseCardVo.getPosition());
				browseEntity.setOwnerCompany(ownerCardVo.getCompany());
				browseEntity.setOwnerPortrait(ownerCardVo.getPortrait());
				templateWebsiteBrowseService.save(browseEntity);
			} else {
				browseEntity.setCompany(browseCardVo.getCompany());
				browseEntity.setPortrait(browseCardVo.getPortrait());
				browseEntity.setName(browseCardVo.getName());
				browseEntity.setPosition(browseCardVo.getPosition());
				browseEntity.setAccessTime(new Date());
				browseEntity.setOwnerCompany(ownerCardVo.getCompany());
				browseEntity.setOwnerPortrait(ownerCardVo.getPortrait());
				templateWebsiteBrowseService.update(browseEntity);
			}
		}

		List<String> browses = templateWebsiteBrowseService.queryListByOwnerOpenid(ownerOpenid).stream().map(TemplateWebsiteBrowseEntity::getPortrait).collect(Collectors.toList());
		resultMap.put("browses", browses);
		return ApiResult.ok(resultMap);
	}
	
	
	@IgnoreAuth
	@ApiOperation("浏览历史")
	@PostMapping("/browse_history")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult browseHistory(@RequestParam String openid,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("browseOpenid", openid);
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<TemplateWebsiteBrowseEntity> browseList = templateWebsiteBrowseService.queryList(query);
		return ApiResult.ok(browseList);
	}
	
	@ApiOperation("官网小程序码")
	@PostMapping("/view_qrcode")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = true)
	})
	public ApiResult viewQrcode(@RequestParam String ownerOpenid) {
		MemberEntity memberEntity = memberService.queryObjectByOpenid(ownerOpenid);
		Assert.isNullApi(memberEntity, "该会员不存在");
		CardEntity cardEntity = cardService.queryObjectByOpenid(ownerOpenid);
		TemplateWebsiteLayoutEntity layoutEntity = templateWebsiteLayoutService
				.queryObjectByMemberId(memberEntity.getId());
		
		if(layoutEntity == null) {
			//获取上级布局
			TemplateWebsiteLayoutEntity superiorLayoutEntity = templateWebsiteLayoutService
					.queryObjectByMemberId(memberEntity.getSuperiorId());
			Assert.isNullApi(superiorLayoutEntity, "该会员未制作官网");
			
			layoutEntity = new TemplateWebsiteLayoutEntity();
			layoutEntity.setCtime(new Date());
			layoutEntity.setMemberId(memberEntity.getId());
			layoutEntity.setMetadataId(superiorLayoutEntity.getMetadataId());
			layoutEntity.setOpenid(memberEntity.getOpenid());
			layoutEntity.setTemplateId(superiorLayoutEntity.getTemplateId());
			layoutEntity.setUtime(new Date());
			templateWebsiteLayoutService.save(layoutEntity);
		}
		HashMap<String, Object> resultMap = Maps.newHashMap();
		if (StringUtils.isNotBlank(layoutEntity.getQrcode())) {
			resultMap.put("qrcode", layoutEntity.getQrcode());
			resultMap.put("company", cardEntity.getCompany());
			return ApiResult.ok(resultMap);
		}
		
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_WEBSITE_DETAIL, memberEntity.getOpenid()), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			layoutEntity.setQrcode(yykjProperties.getImagePrefixUrl().concat(key));
			templateWebsiteLayoutService.update(layoutEntity);
			resultMap.put("qrcode", layoutEntity.getQrcode());
			resultMap.put("company", cardEntity.getCompany());
			return ApiResult.ok(resultMap);
		} catch (WxErrorException e) {
			log.error("===生成官网小程序码异常：{}", e.getMessage());
		}
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@ApiOperation("官网分享图片")
	@PostMapping("/view_share_image")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = true)
	})
	public ApiResult viewShareImage(@RequestParam String ownerOpenid) {
		CardEntity cardEntity = cardService.queryObjectByOpenid(ownerOpenid);
		Assert.isNullApi(cardEntity, "名片不存在");
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
				templateItme.setDescribe(cardEntity.getCompany());
			}
			
			if (templateItme.getId().equals(2602)) {
				templateItme.setDescribe("扫描二维码访问");
				
			}
			
			//小程序码
			if (templateItme.getId().equals(2603)) {
				final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
				try {
					File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
							StrUtil.format(SystemConstant.APP_PAGE_PATH_WEBSITE_DETAIL, ownerOpenid), 280, false, null, false);
					String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
					templateItme.setImagePath(yykjProperties.getImagePrefixUrl().concat(key));
				} catch (WxErrorException e) {
					log.error("===生成官网小程序码异常：{}", e.getMessage());
				}
			}
		}

		File generateShareImage = ProjectUtils.generateShareImage(templateItmeList, templateEntity);
		String shareImageUrl = ProjectUtils.uploadCosFile(cosClient, generateShareImage);
		String fullUrl = yykjProperties.getImagePrefixUrl() + shareImageUrl;
		log.info("==============官网分享图片：{}", fullUrl);
		return ApiResult.ok(fullUrl);
	}
	
}
