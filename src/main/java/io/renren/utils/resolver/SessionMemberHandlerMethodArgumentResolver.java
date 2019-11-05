package io.renren.utils.resolver;

import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;

import io.renren.api.dto.SessionMember;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.interceptor.AuthorizationInterceptor;
import io.renren.utils.validator.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * 有@SessionMember注解的方法参数，注入当前登录用户
 * 
 * @author huyufan
 *
 */
@Slf4j
@Component
public class SessionMemberHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private MemberService memberService;
/*	@Autowired
	private ForbiddenService forbiddenService;*/

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(SessionMember.class)
				&& parameter.hasParameterAnnotation(TokenMember.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory factory) throws Exception {

		// 获取用户ID
		Object object = request.getAttribute(AuthorizationInterceptor.LOGIN_USER_KEY, RequestAttributes.SCOPE_REQUEST);

		if (object == null) {
			return null;
		}

		MemberEntity member = memberService.queryObject((Integer) object);
		Assert.isNullApi(member, "该用户不存在");
		if (null != member) {
			SessionMember sessionMember = new SessionMember();
			sessionMember.setMemberId(member.getId())//
					.setMobile(member.getMobile())//
					.setOpenid(member.getOpenid())//
					.setType(member.getType())//
					.setPortrait(member.getPortrait())//
					.setNickname(member.getNickname());
			log.debug("登入信息 ===> {}", JSON.toJSONString(sessionMember));
		/*	if (StringUtils.equals("freeze", member.getStatus())) {
				String forbiddenMsg = "该账号已被封禁";
				ForbiddenMemberEntity forbiddenMemberEntity = null;
				if(StringUtils.equals(MemberRoleEnum.BOSS.getCode(), member.getRole())) {
					forbiddenMemberEntity = forbiddenService.queryObjectBySuperiorId(member.getId());
				}else {
					forbiddenMemberEntity = forbiddenService.queryObjectByOpenid(member.getOpenid());
				}
				if(forbiddenMemberEntity != null) {
					forbiddenMsg = forbiddenMemberEntity.getForbiddenMsg();
				}
				throw new ApiException(forbiddenMsg, 10001, forbiddenMemberEntity);
			}*/
			return sessionMember;
		}
		return null;
	}
}
