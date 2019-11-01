package io.renren.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.service.SmsService;
import io.renren.utils.HttpContextUtils;
import io.renren.utils.SystemCache;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("短信")
@RestController
@RequestMapping("/api/sms")
public class ApiSmsController {

	@Autowired
	private SmsService smsService;

	@IgnoreAuth
	@PostMapping("/send_code")
	@ApiOperation(value = "发送手机验证码")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true) })
	public ApiResult sendCode(@RequestParam(value = "mobile", required = true) String mobile) {
		if (!Validator.isMobile(mobile)) {
			return ApiResult.error(500, "手机号格式错误");
		}
		String referer = HttpContextUtils.getHttpServletRequest().getHeader("referer");
		log.info("referer===>{}", referer);//https://servicewechat.com/wx69b79496c0342beb
		if(!referer.startsWith("https://servicewechat.com/wx69b79496c0342beb")) {
			return ApiResult.error(500, "非法请求");
		}
		String verificationCodeCache = SystemCache.smsCodeCache.get(mobile);
		if (StringUtils.isNotBlank(verificationCodeCache)) {
			return ApiResult.ok();
		}
		String verificationCode = RandomUtil.randomNumbers(6);
		boolean isSend = smsService.send(mobile, SystemConstant.SMS_TEMPLATE_ID_VERIFICATION_CODE,
				SystemConstant.SMS_SIGN, verificationCode);
		if (isSend) {
			log.info("短信发送成功,验证码:{}", verificationCode);
			SystemCache.smsCodeCache.put(mobile, verificationCode);
			return ApiResult.ok();
		} else {
			return ApiResult.error(500, "短信发送错误");
		}

	}

}
