package io.renren.enums;

/**
 * 审核状态
 * 
 * @author moran
 *
 */
public enum AuditStatusEnum {
	PENDING("pending"), PASS("pass"), REJECT("reject"),UNCOMMIT("uncommit");

	private String code;

	AuditStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
