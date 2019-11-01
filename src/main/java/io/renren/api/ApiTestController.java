package io.renren.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.entity.UserEntity;
import io.renren.properties.YykjProperties;
import io.renren.utils.R;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * API测试接口
 *
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/api")
@Api("测试接口")
@ApiIgnore()
@Slf4j
public class ApiTestController {

	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private ApiLoginController apiLoginController;

	/**
	 * 获取用户信息
	 */
	@GetMapping("userInfo")
	@ApiOperation(value = "获取用户信息")
	@ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true)
	public R userInfo(@LoginUser UserEntity user) {
		return R.ok().put("user", user);
	}

	/**
	 * 忽略Token验证测试
	 */
	@IgnoreAuth
	@GetMapping("notToken")
	@ApiOperation(value = "忽略Token验证测试")
	public R notToken() {
		return R.ok().put("msg", yykjProperties.getUploaddir());
	}

	@IgnoreAuth
	@GetMapping("async")
	@ApiOperation(value = "async测试")
	public R async() {
		log.info("async begin*******************");
		apiLoginController.sleep(5000);
		log.info("async end*******************");
		return R.ok().put("msg", "ok");
	}
}
