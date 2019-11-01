package io.renren.api.vo;

/**
 * 统一接口返回
 * 
 */
public class ApiResult {

	private Integer statusCode = 200;
	private String statusMsg = "success";
	private Object data;

	public static ApiResult ok() {
		return new ApiResult();
	}

	public static ApiResult error(Integer statusCode, String statusMsg) {
		return new ApiResult(statusCode, statusMsg);
	}

	public static ApiResult error(Integer statusCode, String statusMsg, Object data) {
		return new ApiResult(statusCode, statusMsg, data);
	}

	public static ApiResult ok(Object data) {
		return new ApiResult(data);
	}

	public ApiResult(Object data) {
		super();
		this.data = data;
	}

	public ApiResult(Integer statusCode, String statusMsg, Object data) {
		super();
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
		this.data = data;
	}

	public ApiResult(Integer statusCode, String statusMsg) {
		super();
		this.statusCode = statusCode;
		this.statusMsg = statusMsg;
	}

	public ApiResult() {
		super();
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
