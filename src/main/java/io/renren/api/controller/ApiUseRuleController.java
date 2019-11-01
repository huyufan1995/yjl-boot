package io.renren.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.UseRuleEntity;
import io.renren.cms.service.UseRuleService;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
@RestController
@RequestMapping("/api/userule")
@Api("用户使用规则信息")
public class ApiUseRuleController {
	@Autowired
	private UseRuleService useRuleService;

	@IgnoreAuth
	@ApiOperation(value = "使用规则信息", notes = "使用规则信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "type", required = true) })
	@PostMapping("/get_rule")
	public ApiResult getRule(@ApiIgnore() @RequestParam Map<String, Object> params) {
		String type = (String) params.get("type");
		Assert.isBlank(type, "type为空");
		UseRuleEntity rule = useRuleService.queryObjectByType(type);
		return ApiResult.ok(rule);
	}

}
