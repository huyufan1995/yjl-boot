package io.renren.api.controller;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/open/api")
@Api("开放API")
public class ApiOpenController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private YykjProperties yykjProperties;

	@ApiOperation(value = "开通VIP")
	@PostMapping("/enable_vip")
	public ApiResult enableVip(@RequestParam String mobile, @RequestParam String key) {

		if (StringUtils.isBlank(key) || !StringUtils.equals("yykj@key", key)) {
			return ApiResult.error(500, "非法请求");
		}

		if (!Validator.isMobile(mobile)) {
			return ApiResult.error(500, "手机号格式错误");
		}

		memberService.enableVip(mobile);
		return ApiResult.ok();
	}

	@GetMapping("/enable_qrcode/{mobile}")
	public void enableQrcode(@PathVariable("mobile") String mobile, HttpServletResponse response) throws Exception {
		if (!Validator.isMobile(mobile)) {
			return;
		}
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		MemberEntity memberEntity = memberService.queryObjectByMobile(mobile);
		if(memberEntity == null) {
			return;
		}
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		//生成领取码
		File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
				StrUtil.format(SystemConstant.APP_PAGE_PATH_ACTIVATE_VIP, memberEntity.getCode()), 280, false, null,
				false);

		BufferedImage image = ImageIO.read(qrcodeFile);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		out.flush();
	}

}
