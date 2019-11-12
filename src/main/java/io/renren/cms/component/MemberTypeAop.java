package io.renren.cms.component;

import io.renren.api.constant.SystemConstant;
import io.renren.api.exception.ApiException;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import io.renren.enums.MemberTypeEnum;
import io.renren.utils.annotation.MemberType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断用户是否认证，只有认证后才可以做一些入库操作
 */
@Aspect
@Component
public class MemberTypeAop {

	@Autowired
	private MemberService memberService;

    @Pointcut("execution(public * io.renren.api.controller.*.add*(..))")
	public void memberTypeAop() {
	}


	@Before("memberTypeAop()")
	public void before(JoinPoint joinPoint){
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		MemberType memberType = signature.getMethod().getDeclaredAnnotation(MemberType.class);
		if (memberType != null) {
			// 获取用户ID
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			Object object = request.getAttribute("LOGIN_USER_KEY");

			if (object == null) {
				throw new ApiException("用户为空", 100011);
			}

			MemberEntity member = memberService.queryObject((Integer) object);
			if(MemberTypeEnum.COMMON.getCode().toLowerCase().equals(member.getType())){
				throw new ApiException(SystemConstant.MEMBER_TYPE_MSG, 10001);
			}
		}

	}


}
