package io.renren.api.exception;

/**
 * 自定义接口异常
 * 
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;
	private Object data;

	public ApiException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public ApiException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public ApiException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public ApiException(String msg, int code, Object data) {
		super(msg);
		this.msg = msg;
		this.code = code;
		this.data = data;
	}

	public ApiException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public ApiException(String msg, int code, Object data, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.data = data;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
