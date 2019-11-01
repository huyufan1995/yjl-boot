package io.renren.enums;

/**
 * 权限类型
 * @author yujia
 *
 */
public enum PermTypeEnum {
	EMPLOY("employ"), LEADER("leader");

	private String code;

	PermTypeEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
