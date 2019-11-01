package io.renren.enums;

/**
 * 订单类型
 * @author yujia
 *
 */
public enum OrderTypeEnum {
	NEWBUY("newbuy"), UPGRADE("upgrade"), RENEWAL("renewal");

	private String code;

	OrderTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
