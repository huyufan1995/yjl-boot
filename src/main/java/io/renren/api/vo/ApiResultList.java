package io.renren.api.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统一接口返回
 * 
 */
@Data
@ApiModel(value = "集合")
public class ApiResultList<T> {

	private Integer statusCode = 200;
	private String statusMsg = "success";
	@ApiModelProperty(value = "数据")
	private T data;

	public static ApiResultList ok() {
		return new ApiResultList();
	}

	public static ApiResultList error(Integer statusCode, String statusMsg) {
		return new ApiResultList(statusCode, statusMsg);
	}

	public  ApiResultList error(Integer statusCode, String statusMsg, T data) {
		return new ApiResultList(statusCode, statusMsg, data);
	}

	public  ApiResultList ok(T data) {
		return new ApiResultList(data);
	}

	public ApiResultList(T data) {
		super();
		this.data = data;
	}

	public ApiResultList(Integer statusCode, String statusMsg, T data) {
		super();
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
		this.data = data;
	}

	public ApiResultList(Integer statusCode, String statusMsg) {
		super();
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
	}

	public ApiResultList() {
		super();
	}

}
