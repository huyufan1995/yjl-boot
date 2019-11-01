package io.renren.api.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mysql.jdbc.MysqlDataTruncation;

import io.renren.api.vo.ApiResult;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 接口异常处理器
 * 
 */
@RestControllerAdvice
public class ApiExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 微信接口异常
	 */
	@ExceptionHandler(WxErrorException.class)
	public ApiResult handleWxErrorException(WxErrorException e) {
		logger.error("WxErrorException ---> {}", e.getError().getErrorMsg());
		return ApiResult.error(e.getError().getErrorCode(), e.getError().getErrorMsg());
	}

	/**
	 * 自定义接口异常
	 */
	@ExceptionHandler(ApiException.class)
	public ApiResult handleApiException(ApiException e) {
		logger.error("ApiException ---> {}", e.getMsg());
		return ApiResult.error(e.getCode(), e.getMsg(), e.getData());
	}

	/**
	 * 非法参数验证异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public ApiResult handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		List<String> list = new ArrayList<>();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			list.add(fieldError.getDefaultMessage());
		}
		String errorMsgs = StringUtils.join(list.toArray(), ",");
		logger.error("请求参数校验异常 ---> {}", errorMsgs);
		return ApiResult.error(500, errorMsgs);
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public ApiResult handleBindExceptionHandler(BindException e) {
		BindingResult bindingResult = e.getBindingResult();
		List<String> list = new ArrayList<>();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			list.add(fieldError.getDefaultMessage());
		}
		String errorMsgs = StringUtils.join(list.toArray(), ",");
		logger.error("请求参数校验异常 ---> {}", errorMsgs);
		return ApiResult.error(500, errorMsgs);
	}

	/**
	 * 请求参数解析异常
	*/
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.OK)
	public ApiResult httpMessageNotReadableException(HttpMessageNotReadableException e) {
		logger.error("请求参数解析异常 ---> {}", e.getMessage());
		return ApiResult.error(500, "HttpMessageNotReadableException");
	}

	/**
	 * HTTP Media 类型异常
	 */
	@ExceptionHandler(value = HttpMediaTypeException.class)
	@ResponseStatus(HttpStatus.OK)
	public ApiResult httpMediaTypeException(HttpMediaTypeException e) {
		logger.error("HttpMediaTypeException 类型异常 ---> {}", e.getMessage());
		return ApiResult.error(500, "HttpMediaTypeException");
	}

	/**
	 * 数据库超出限制类型异常
	 */
	@ExceptionHandler(value = MysqlDataTruncation.class)
	@ResponseStatus(HttpStatus.OK)
	public ApiResult mysqlDataTruncationException(MysqlDataTruncation e) {
		logger.error("MysqlDataTruncation 类型异常 ---> {}", e.getMessage());
		return ApiResult.error(500, "数据长度超出限制");
	}

}
