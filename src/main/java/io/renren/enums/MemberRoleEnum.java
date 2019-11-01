package io.renren.enums;

/**
 * 会员角色
 * 
 * @author yujia
 *
 */
public enum MemberRoleEnum {
	BOSS("boss"), ADMIN("admin"), STAFF("staff");

	private String code;

	MemberRoleEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
