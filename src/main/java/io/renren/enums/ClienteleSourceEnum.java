package io.renren.enums;

/**
 * 客户来源
 * @author yujia
 *
 */
public enum ClienteleSourceEnum {
	OTHER("other", "其它"), APPLY("apply", "活动表单"), MANUAL("manual", "手工录入"), CARD("card", "智能名片"), WEBSITE("website",
			"微官网"), CASES("cases", "案例库"), IMPORTS("imports", "系统导入");

	private String code;
	private String des;

	ClienteleSourceEnum(String code, String des) {
		this.code = code;
		this.des = des;
	}

	public static String getDes(String code) {
		ClienteleSourceEnum[] clienteleSourceEnums = values();
		for (ClienteleSourceEnum clienteleSourceEnum : clienteleSourceEnums) {
			if (clienteleSourceEnum.code.equals(code)) {
				return clienteleSourceEnum.des;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getDes() {
		return des;
	}

}
