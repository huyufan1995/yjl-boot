package io.renren.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.*;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.entity.CollectEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.*;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.AuditStatusEnum;
import io.renren.enums.MemberTypeEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import io.renren.api.vo.ApiResult;
import io.renren.utils.annotation.IgnoreAuth;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员
 * 
 * @author huyufan
 * @date 2019-11-6 13:51:48
 */
@Api("会员")
@Slf4j
@RestController
@RequestMapping("/api/member")
public class ApiMemberController {

	@Autowired
	private COSClient cosClient;

	@Autowired
	private YykjProperties yykjProperties;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CollectService collectService;

	@Autowired
	private ApplyRecordService applyRecordService;

	@Value("${tencent.cos.imagePrefixUrl}")
	private String imagePrefixUrl;

	@Autowired
	private ApplyService applyService;

	@Autowired
	private InformationBrowsService informationBrowsService;

	@Autowired
	private InformationService informationService;
	
	@IgnoreAuth
	@PostMapping("/login")
	@ApiOperation(value = "会员登入【调用完微信登入后再调用该接口】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true)
	})
	public ApiResult login(@RequestParam String openid) {
		SessionMember sessionMember = memberService.login(openid);
		return ApiResult.ok(sessionMember);
	}

/*	@IgnoreAuth
	@PostMapping("/info")
	@ApiOperation(value = "会员信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true)
	})
	public ApiResult info(@RequestParam String openid) {
		MemberEntity memberEntity = memberService.queryObjectByOpenid(openid);
		return ApiResult.ok(memberEntity);
	}*/


	@IgnoreAuth
	@ApiOperation(value = "会员活动报名列表", notes = "会员活动报名列表")
	@PostMapping("/member_apply_record_list")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true)
	})
	public ApiResult memberApplyList(@ApiIgnore() @RequestParam Map<String, Object> params, @RequestParam String openid) {
		Query query = new Query(params);
		List<ApplyEntityDto> applyEntityListDto = applyService.queryListByOpenId(query);
		//对内容过滤html标签，并确认是否是50字以内
		for (ApplyEntityDto entityDto : applyEntityListDto) {
			String content = HTMLSpirit.getTextFromHtml(entityDto.getApplyContent());
			if (content.length() > 50) {
				content = content.substring(0, 50);
			}
			entityDto.setApplyContent(content);
			HashMap<String, Object> q = new HashMap<>(3);
			q.put("applyId", entityDto.getId());
			q.put("offset", 0);
			q.put("limit", 4);
			List<ApplyRecordEntiyDto> applyRecordEntiyDtoList = applyRecordService.queryPortrait(q);
			// 处理报名人头像
			entityDto.setPortrait(applyRecordEntiyDtoList.stream().map(ApplyRecordEntiyDto::getPortrait).collect(Collectors.toList()));
			entityDto.setApplyStatus("已报名");
		}
		return ApiResult.ok(applyEntityListDto);
	}


	@IgnoreAuth
	@ApiOperation(value = "会员收藏列表", notes = "会员收藏列表")
	@PostMapping("/member_collect_list")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "collectType", value = "collectType  1：资讯 3：活动 4：会员", required = true)
	})
	public ApiResult memberCollectList(@ApiIgnore() @RequestParam Map<String, Object> params, @RequestParam String openid,@RequestParam Integer collectType) {
		Query query = new Query(params);
		//查询活动
		if(collectType == 3){
			List<ApplyEntityDto> applyEntityListDto = applyService.queryListByOpenIdWithCollect(query);
			//对内容过滤html标签，并确认是否是50字以内
			for (ApplyEntityDto entityDto : applyEntityListDto) {
				String content = HTMLSpirit.getTextFromHtml(entityDto.getApplyContent());
				if (content.length() > 50) {
					content = content.substring(0, 50);
				}
				entityDto.setApplyContent(content);
				HashMap<String, Object> q = new HashMap<>(3);
				q.put("applyId", entityDto.getId());
				q.put("offset", 0);
				q.put("limit", 4);
				List<ApplyRecordEntiyDto> applyRecordEntiyDtoList = applyRecordService.queryPortrait(q);
				// 处理报名人头像
				entityDto.setPortrait(applyRecordEntiyDtoList.stream().map(ApplyRecordEntiyDto::getPortrait).collect(Collectors.toList()));
				if (new Date().after(entityDto.getStartTime())) {
					entityDto.setApplyStatus("已结束");
				} else {
					if (applyRecordEntiyDtoList.isEmpty()) {
						entityDto.setApplyStatus("立即报名");

					} else {
						for (ApplyRecordEntiyDto recordEntiyDto : applyRecordEntiyDtoList) {
							if (params.get("openid").toString().equals(recordEntiyDto.getOpenid())) {
								entityDto.setApplyStatus("已报名");
							}
						}
					}
				}
			}
			return ApiResult.ok(applyEntityListDto);
		}else if (collectType == 1){
			List<InformationsEntityDto> informationList = informationService.queryListDtoByOpenIdWithCollect(query);
			//对内容过滤html标签，并确认是否是50字以内
			for (InformationsEntityDto entityDto : informationList) {
				String content = HTMLSpirit.getTextFromHtml(entityDto.getContent());
				if(content.length()>50){
					content=content.substring(0,50);
				}
				entityDto.setContent(content);
				List<String> portrait = informationBrowsService.queryPortrait(entityDto.getId().toString());
				entityDto.setPortrait(portrait);
			}
			return ApiResult.ok(informationList);
		}
		return null;
	}


	@IgnoreAuth
	@PostMapping("/authentication")
	@ApiOperation(value = "会员认证")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "memberId", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "gender", value = "性别", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "portrait", value = "头像", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "nickname", value = "姓名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机1", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobileCountry", value = "手机1所属地区", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "phone", value = "手机2", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "phoneCountry", value = "手机2所属地区", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "email", value = "email", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "weixinNumber", value = "微信号", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "birthday", value = "出生年月日", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "nationality", value = "国籍", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "profile", value = "个人简介", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "address", value = "现居住地址", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "companyProfile", value = "公司简介", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "haveResource", value = "拥有资源", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "needResource", value = "需要资源", required = false)
	})
	public ApiResult info(@ApiIgnore()MemberEntity memberEntity) {
		memberService.update(memberEntity);
		return ApiResult.ok(memberEntity);
	}


	/**
	 * 会员更改头像
	 */
	@IgnoreAuth
	@ApiOperation(value = "会员更改头像")
	@PostMapping(value = "/portrait", headers = "content-type=multipart/form-data")
	public ApiResult cloudImage(@ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile multipartFile) {

		HashMap<String, Object> result = Maps.newHashMap();
		try {

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

			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			// 转换后会生成临时文件
			ProjectUtils.inputStreamToFile(inputStream, tempFile);

			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			if(!wxMaService.getSecCheckService().checkImage(tempFile)) {
				return ApiResult.error(500, "图片含有违法违规内容");
			}

			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			// 文件路径
			String path = "yjl/app/portrait/" + uuid;
			String key = path + extName;
			PutObjectRequest putObjectRequest = new PutObjectRequest(SystemConstant.BUCKET_NAME_IMAGE, key, tempFile);
			// 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
			putObjectRequest.setStorageClass(StorageClass.Standard);
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
			// putobjectResult会返回文件的etag
			String etag = putObjectResult.getETag();
			log.info("etag ===> {}", etag);
			// 删除临时文件
			File delFile = new File(tempFile.toURI());
			delFile.delete();
			if (StringUtils.isNotBlank(etag)) {
				result.put("key", key);
				result.put("url", imagePrefixUrl.concat(key));
				log.info("COS 上传文件成功 ===> {}", result);
				return ApiResult.ok(result);
			} else {
				return ApiResult.error(500, "上传失败");
			}

		} catch (Exception e) {
			log.error("COS上传文件异常 === ", e);
			return ApiResult.error(500, "上传失败");
		}
	}


	@IgnoreAuth
	@ApiOperation("会员二维码")
	@PostMapping("/qrcode")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "会员id", required = true)
	})
	public ApiResult applyDataInfo(@RequestParam Integer id) {
		MemberEntity memberEntity = memberService.queryObject(id);
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_ACTIVATE_VIP, memberEntity.getCode()), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			System.err.println(yykjProperties.getImagePrefixUrl().concat(key));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===生成会员邀请二维码异常：{}", e.getMessage());
		}
		return null;
	}


}
