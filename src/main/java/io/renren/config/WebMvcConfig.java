package io.renren.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.renren.utils.interceptor.AuthorizationInterceptor;
import io.renren.utils.resolver.LoginUserHandlerMethodArgumentResolver;
import io.renren.utils.resolver.SessionMemberHandlerMethodArgumentResolver;

/**
 * MVC配置
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private AuthorizationInterceptor authorizationInterceptor;
	@Autowired
	private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;
	@Autowired
	private SessionMemberHandlerMethodArgumentResolver sessionMemberHandlerMethodArgumentResolver;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
		argumentResolvers.add(sessionMemberHandlerMethodArgumentResolver);
	}
}
