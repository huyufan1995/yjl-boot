package io.renren.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.WXAdvEntity;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/adv")
@Api("广告接口")
public class ApiAdvController {

	@IgnoreAuth
	@ApiOperation(value = "微信广告信息")
	@PostMapping("/info")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "location", value = "位置", required = true) })
	public ApiResult info(String location) {
		Assert.isBlankApi(location, "位置不能为空");
		return ApiResult.ok(new WXAdvEntity(1, "adunit-e43844933a951dd7", location, "t"));
	}

}
