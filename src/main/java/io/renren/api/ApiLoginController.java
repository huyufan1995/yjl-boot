package io.renren.api;

import io.renren.service.TokenService;
import io.renren.service.UserService;
import io.renren.utils.R;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * API登录授权
 *
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2017-03-23 15:31
 */
@RestController
@RequestMapping("/api")
@Api("登录接口")
@ApiIgnore()
@Slf4j
public class ApiLoginController {
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	/**
	 * 登录
	 */
	@IgnoreAuth
	@PostMapping("login")
	@ApiOperation(value = "登录", notes = "登录说明")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "password", value = "密码", required = true) })
	public R login(String mobile, String password) {
		Assert.isBlank(mobile, "手机号不能为空");
		Assert.isBlank(password, "密码不能为空");

		//用户登录
		Integer userId = userService.login(mobile, password);

		//生成token
		Map<String, Object> map = tokenService.createToken(userId);

		return R.ok(map);
	}

	@Async
	public void sleep(long t) {
		try {
			log.info("sleep begin **********");
			Thread.sleep(t);
			log.info("sleep end **********");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
