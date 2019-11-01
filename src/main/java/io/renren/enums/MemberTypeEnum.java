package io.renren.enums;

/**
 * 会员类型
 * 
 * @author yujia
 *
 */
public enum MemberTypeEnum {
	COMMON("common"), // 普通
	VIP("vip");// vip

	private String code;

	MemberTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
