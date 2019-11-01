package io.renren.enums;

/**
 * 审核状态
 * 
 * @author yujia
 *
 */
public enum BindStatusEnum {
	NOT("not"), UNBIND("unbind"), BIND("bind");

	private String code;

	BindStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
