package io.renren.api.controller;

import io.renren.cms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 会员
 * 
 * @author huyufan
 * @date 2019-11-6 13:51:48
 */
@Api("会员")
@RestController
@RequestMapping("/api/member")
public class ApiMemberController {

	@Autowired
	private MemberService memberService;
	
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
}
