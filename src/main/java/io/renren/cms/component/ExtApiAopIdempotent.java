/*
package io.renren.cms.component;

import io.renren.utils.RedisUtils;
import io.renren.utils.annotation.ExtApiIdempotent;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@Aspect
@Component
public class ExtApiAopIdempotent {
	@Autowired
	private RedisUtils redisUtils;

	@Pointcut("execution(public * io.renren.api.controller.*.*(..))")
	public void rlAop() {
	}


	// 环绕通知验证参数
	@Around("rlAop()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		ExtApiIdempotent extApiIdempotent = signature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
		if (extApiIdempotent != null) {
			return extApiIdempotent(proceedingJoinPoint, signature);
		}
		// 放行
		Object proceed = proceedingJoinPoint.proceed();
		return proceed;
	}

	// 验证Token
	public Object extApiIdempotent(ProceedingJoinPoint proceedingJoinPoint, MethodSignature signature)
			throws Throwable {
		ExtApiIdempotent extApiIdempotent = signature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
		if (extApiIdempotent == null) {
			// 直接执行程序
			Object proceed = proceedingJoinPoint.proceed();
			return proceed;
		}
		// 代码步骤：
		// 1.获取令牌 存放在请求头中
		HttpServletRequest request = getRequest();
		String valueType = extApiIdempotent.type();
		if (StringUtils.isEmpty(valueType)) {
			response("参数错误!");
			return null;
		}
		String token = null;
		if (valueType.equals("head")) {
			//extApiIdempotent
			token = request.getHeader("token");
		} else {
			token = request.getParameter("token");
		}
		if (StringUtils.isEmpty(token)) {
			response("参数错误!");
			return null;
		}
		if (StringUtils.isEmpty(redisUtils.get(token))) {
			response("请勿重复提交!");
			return null;
		}
		Object proceed = proceedingJoinPoint.proceed();
		redisUtils.delete(token);
		return proceed;
	}

	public void extApiToken() {
		String token =  UUID.randomUUID().toString();
		redisUtils.set("token",token,30000);
		getRequest().setAttribute("token", token);

	}

	public HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		return request;
	}

	public void response(String msg) throws IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = attributes.getResponse();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			writer.println(msg);
		} catch (Exception e) {

		} finally {
			writer.close();
		}

	}

}*/
