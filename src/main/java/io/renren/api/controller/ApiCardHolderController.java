package io.renren.api.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.xsshome.taip.ocr.TAipOcr;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.CardHolderAccessRecordEntity;
import io.renren.cms.entity.CardHolderEntity;
import io.renren.cms.entity.ClienteleEntity;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.CardHolderAccessRecordService;
import io.renren.cms.service.CardHolderService;
import io.renren.cms.service.CardService;
import io.renren.cms.service.ClienteleService;
import io.renren.cms.service.DeptMemberService;
import io.renren.cms.service.MemberService;
import io.renren.enums.MemberRoleEnum;
import io.renren.utils.PinyinUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 名片夹
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 17:23:12
 */
@Api("名片夹")
@RestController
@RequestMapping("/api/cardholder")
public class ApiCardHolderController {

	@Autowired
	private CardService cardService;
	@Autowired
	private CardHolderService cardHolderService;
	@Autowired
	private CardHolderAccessRecordService cardHolderAccessRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ClienteleService clienteleService;
	@Autowired
	private TAipOcr tAipOcr;
	@Autowired
	private DeptMemberService deptMemberService;
	
	@IgnoreAuth
	@PostMapping("/info")
	@ApiOperation(value = "获取名片夹详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "cardHolderId", value = "名片夹ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "访问人openid", required = true)
	})
	public ApiResult info(@RequestParam Integer cardHolderId, @RequestParam String openid) {
		CardHolderEntity cardHolderEntity = cardHolderService.queryObject(cardHolderId);
		Assert.isNullApi(cardHolderEntity, "该名片夹不存在");
		
		//添加名片夹访问记录
		if(!StringUtils.equals(openid, cardHolderEntity.getOpenid())) {
			CardHolderAccessRecordEntity accessRecord = cardHolderAccessRecordService.queryObjectByOpenidAndCardHolderId(cardHolderEntity.getId(), openid);
			CardEntity cardEntity = cardService.queryObjectByOpenid(openid);
			if(accessRecord == null) {
				accessRecord = new CardHolderAccessRecordEntity();
				accessRecord.setAccessTime(new Date());
				accessRecord.setCardHolderId(cardHolderEntity.getId());
				accessRecord.setCardHolderOpenid(cardHolderEntity.getOpenid());
				accessRecord.setCtime(new Date());
				accessRecord.setOpenid(openid);
				if(cardEntity != null) {
					accessRecord.setCompany(cardEntity.getCompany());
					accessRecord.setName(cardEntity.getName());
					accessRecord.setPortrait(cardEntity.getPortrait());
					accessRecord.setPosition(cardEntity.getPosition());
				} else {
					accessRecord.setCompany(SystemConstant.DEFAULT_CARD_COMPANY);
					accessRecord.setName(SystemConstant.DEFAULT_CARD_NAME);
					accessRecord.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
					accessRecord.setPosition(SystemConstant.DEFAULT_CARD_POSITION);
				}
				cardHolderAccessRecordService.save(accessRecord);
			} else {
				accessRecord.setCompany(cardEntity.getCompany());
				accessRecord.setName(cardEntity.getName());
				accessRecord.setPortrait(cardEntity.getPortrait());
				accessRecord.setPosition(cardEntity.getPosition());
				accessRecord.setAccessTime(new Date());
				cardHolderAccessRecordService.update(accessRecord);
			}
		}
		
		//访问记录
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("cardHolderId", cardHolderEntity.getId());
		params.put("sidx", "access_time");
		params.put("order", "desc");
		params.put("offset", 0);
		params.put("limit", 5);
		List<CardHolderAccessRecordEntity> accessRecordList = cardHolderAccessRecordService.queryList(params);
		cardHolderEntity.setAccessPortraits(accessRecordList.stream().map(CardHolderAccessRecordEntity::getPortrait).collect(Collectors.toList()));
		
		//是否已经添加到客户 根据手机号识别
		MemberEntity visitMember = memberService.queryObjectByOpenid(openid);
		if(visitMember != null) {
			ClienteleEntity clienteleEntity = clienteleService.queryObjectByMobileAndMemberId(cardHolderEntity.getMobile(), visitMember.getId());
			cardHolderEntity.setSaveCrm(clienteleEntity != null ? true : false);
		}
		return ApiResult.ok(cardHolderEntity);
	}
	
	@IgnoreAuth
	@PostMapping("/delete")
	@ApiOperation(value = "删除名片夹")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "cardHolderId", value = "名片夹ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "操作人openid", required = true)
	})
	public ApiResult delete(@RequestParam Integer cardHolderId, @RequestParam String openid) {
		CardHolderEntity cardHolderEntity = cardHolderService.queryObject(cardHolderId);
		Assert.isNullApi(cardHolderEntity, "该名片夹不存在");
		if(!StringUtils.equals(cardHolderEntity.getOpenid(), openid)) {
			return ApiResult.error(500, "无权操作");
		}
		cardHolderService.logicDel(cardHolderId);
		return ApiResult.ok();
	}

	@IgnoreAuth
	@PostMapping("/modify")
	@ApiOperation(value = "修改名片夹【添加或者修改名片夹名片】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "名片夹ID【添加不传，修改传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "portrait", value = "头像", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "姓名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "company", value = "公司", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "position", value = "职位", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "weixin", value = "微信号", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "email", value = "邮箱", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "address", value = "地址", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "addressLongitude", value = "经度", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "addressLatitude", value = "纬度", required = false)
	})
	public ApiResult modify(@ApiIgnore @Validated CardHolderEntity cardHolderEntity) {
		Date now = new Date();
		MemberEntity memberEntity = memberService.queryObjectByOpenid(cardHolderEntity.getOpenid());
		if (cardHolderEntity.getId() == null) {
			if(StringUtils.isBlank(cardHolderEntity.getPortrait())) {
				cardHolderEntity.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
			}
			if(StringUtils.isBlank(cardHolderEntity.getPosition())) {
				cardHolderEntity.setPosition(SystemConstant.DEFAULT_CARD_POSITION);
			}
			cardHolderEntity.setCtime(now);
			cardHolderEntity.setUtime(now);
			cardHolderEntity.setIsDel(SystemConstant.F_STR);
			cardHolderEntity.setFirstLetter(
					PinyinUtils.getPinYinHeadChar(cardHolderEntity.getName()).substring(0, 1).toUpperCase());
			cardHolderEntity.setMemberId(memberEntity == null ? null : memberEntity.getId());
			cardHolderService.save(cardHolderEntity);
		} else {
			CardHolderEntity cardHolderEntityDB = cardHolderService.queryObject(cardHolderEntity.getId());
			Assert.isNullApi(cardHolderEntityDB, "该名片夹不存在");
			cardHolderEntity.setUtime(now);
			cardHolderEntity.setMemberId(memberEntity == null ? null : memberEntity.getId());
			cardHolderEntity.setFirstLetter(
					PinyinUtils.getPinYinHeadChar(cardHolderEntity.getName()).substring(0, 1).toUpperCase());
			cardHolderService.update(cardHolderEntity);
		}
		return ApiResult.ok();
	}

	@IgnoreAuth
	@PostMapping("/all")
	@ApiOperation(value = "名片夹列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司, dept部门", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult all(@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam String openid,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "limit", defaultValue = "5") Integer limit) {
		HashMap<String, Object> params = Maps.newHashMap();
		if(StringUtils.equals(type, "my")) {
			params.put("openid", openid);
		} else if (StringUtils.equals(type, "company")){
			MemberEntity memberEntity = memberService.queryObjectByOpenid(openid);
			if(MemberRoleEnum.BOSS.getCode().equals(memberEntity.getRole())) {
				memberEntity.setSuperiorId(memberEntity.getId());
			}
			params.put("superiorId", memberEntity.getSuperiorId());
		} else if(StringUtils.equals(type, "dept")) {
			MemberEntity memberEntity = memberService.queryObjectByOpenid(openid);
			DeptMemberEntity deptMemberEntity = deptMemberService.queryObjectByMemberId(memberEntity.getId());
			params.put("deptId", deptMemberEntity.getDeptId());
		}
		params.put("key", key);
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<CardHolderEntity> cardHolderList = cardHolderService.queryList(query);
		int cardHolderCount = cardHolderService.queryTotal(query);
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("cardHolderList", cardHolderList);
		resultMap.put("cardHolderCount", cardHolderCount);
		return ApiResult.ok(resultMap);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "解析名片图片")
	@PostMapping(value = "/parse_card_img")
	public ApiResult parseCardImg(
			@ApiParam(value = "名片图片文件", required = true) @RequestParam("file") MultipartFile multipartFile)
			throws Exception {

		if (multipartFile == null) {
			return ApiResult.error(500, "文件不能为空");
		}

		if (multipartFile.getSize() < 0 || multipartFile.getSize() > SystemConstant.MAX_UPLOAD_FILE_SIZE) {
			// 上传图片不能为空或超过3MB
			return ApiResult.error(500, "文件不能超过3MB");
		}

		// 获取上传的文件名
		String fileName = multipartFile.getOriginalFilename();
		// 获取文件的后缀名
		String extName = fileName.substring(fileName.lastIndexOf("."));
		if (SystemConstant.IMAGE_REG.indexOf(extName.toLowerCase()) == -1) {
			return ApiResult.error(500, "必须是图片类型文件");
		}

		String result = tAipOcr.bcOcr(multipartFile.getBytes());
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getIntValue("ret") != 0) {
			return ApiResult.error(500, resultJson.getString("msg"));
		}
		JSONArray itemList = resultJson.getJSONObject("data").getJSONArray("item_list");
		HashMap<String, String> resultMap = Maps.newHashMap();
		for (int i = 0; i < itemList.size(); i++) {
			resultMap.put(itemList.getJSONObject(i).getString("item"),
					itemList.getJSONObject(i).getString("itemstring"));
		}
		
		HashMap<String, String> returnMap = Maps.newHashMap();
		returnMap.put("name", resultMap.get("姓名"));
		returnMap.put("mobile", resultMap.get("手机"));
		returnMap.put("company", resultMap.get("公司"));
		returnMap.put("position", resultMap.remove("职位"));
		returnMap.put("email", resultMap.get("邮箱"));
		returnMap.put("weixin", resultMap.get("微信"));
		return ApiResult.ok(returnMap);
	}

}
