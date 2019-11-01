package io.renren.enums;

/**
 * 审核状态
 * 
 * @author yujia
 *
 */
public enum AuditStatusEnum {
	PENDING("pending"), PASS("pass"), REJECT("reject");

	private String code;

	AuditStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
