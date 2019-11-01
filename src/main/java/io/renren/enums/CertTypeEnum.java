package io.renren.enums;

/**
 * 认证类型
 * 
 * @author yujia
 *
 */
public enum CertTypeEnum {
	UNKNOWN("unknown"), PERSONAGE("personage"), ENTERPRISE("enterprise");

	private String code;

	CertTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
