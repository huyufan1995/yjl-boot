package io.renren.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.WxUserService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wxUser")
@Api("微信")
@Slf4j
public class ApiWxUserController {

	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private YykjProperties yykjProperties;


	@IgnoreAuth
	@PostMapping("/login")
	@ApiOperation(value = "微信登入【进入小程序调用】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "wxcode", value = "code", required = true) })
	public ApiResult login(@RequestParam String wxcode) {
		final WxMaService wxService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(wxcode);
			log.info("微信登录信息 ===> {}", JSON.toJSONString(session));
			WxUserEntity wxUserEntity = wxUserService.queryByOpenId(session.getOpenid());
			Date now = new Date();
			if (null == wxUserEntity) {
				wxUserEntity = new WxUserEntity();
				wxUserEntity.setCtime(now);
				wxUserEntity.setLoginTime(now);
				wxUserEntity.setOpenId(session.getOpenid());
				wxUserEntity.setSessionKey(session.getSessionKey());
				wxUserService.save(wxUserEntity);
			} else {
				WxUserEntity temp = new WxUserEntity();
				temp.setId(wxUserEntity.getId());
				temp.setLoginTime(now);
				temp.setSessionKey(session.getSessionKey());
				wxUserService.update(temp);
			}
			Map<String,String> map = new HashMap<>();
			map.put("openid",wxUserEntity.getOpenId());
			return ApiResult.ok(map);
		} catch (WxErrorException e) {
			log.error(e.getMessage());
			return ApiResult.error(500, "微信登入错误");
		}

	}

	@IgnoreAuth
	@PostMapping("/info")
	@ApiOperation(value = "获取微信用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "微信用户ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "signature", value = "使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "rawData", value = "不包括敏感信息的原始数据字符串，用于计算签名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "iv", value = "加密算法的初始向量", required = true) })
	public ApiResult info(@RequestParam String openid, @RequestParam String signature, @RequestParam String rawData, @RequestParam String encryptedData, @RequestParam String iv) {

		WxUserEntity wxUserEntity = wxUserService.queryByOpenId(openid);
		Assert.isNullApi(wxUserEntity, "无法获取用户");
		final WxMaService wxService = WxMaConfiguration.getMaService(yykjProperties.getAppid());

		// 用户信息校验
		if (!wxService.getUserService().checkUserInfo(wxUserEntity.getSessionKey(), rawData, signature)) {
			log.error("获取微信用户信息验证不通过");
//			return ApiResult.error(500, "检验错误");
		}

		// 解密用户信息
		WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(wxUserEntity.getSessionKey(), encryptedData, iv);
		log.info("获取微信用户信息结果 ===> {}", JSON.toJSONString(userInfo));
		// 复制新的所有非null来覆盖旧的
		BeanUtil.copyProperties(userInfo, wxUserEntity, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		wxUserService.update(wxUserEntity);
		return ApiResult.ok(wxUserEntity);
	}


	@IgnoreAuth
	@PostMapping("/phone")
	@ApiOperation(value = "获取用户绑定手机号信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "微信用户ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "signature", value = "使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "rawData", value = "不包括敏感信息的原始数据字符串，用于计算签名", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "iv", value = "加密算法的初始向量", required = true) })
	public ApiResult phone(@RequestParam("openid") String openid,
						   @RequestParam(value = "signature", required = false) String signature,
						   @RequestParam(value = "rawData", required = false) String rawData,
						   @RequestParam("encryptedData") String encryptedData,
						   @RequestParam("iv") String iv) {

		WxUserEntity wxUserEntity = wxUserService.queryByOpenId(openid);
		Assert.isNullApi(wxUserEntity, "用户不存在");
		final WxMaService wxService = WxMaConfiguration.getMaService(yykjProperties.getAppid());

		// 用户信息校验
		if (!wxService.getUserService().checkUserInfo(wxUserEntity.getSessionKey(), rawData, signature)) {
			log.error("获取微信用户手机号验证不通过 ===");
//			return ApiResult.error(500, "检验错误");
		}

		// 解密
		WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(wxUserEntity.getSessionKey(), encryptedData, iv);
		log.info("获取微信用户手机号结果 ===> {}", JSON.toJSONString(phoneNoInfo));
		WxUserEntity temp = new WxUserEntity();
		temp.setId(wxUserEntity.getId());
		temp.setMobile(phoneNoInfo.getPurePhoneNumber());// 没有区号的手机号
		wxUserService.update(temp);
		return ApiResult.ok(temp.getMobile());
	}


}