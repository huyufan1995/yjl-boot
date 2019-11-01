package io.renren.enums;

/**
 * 模板消息
 * @author yujia
 *
 */
public enum TemplateMsgTypeEnum {

	BUYVIP("buyvip", "VIP开通成功通知", "6Bb3s38iVPo6o7kTlt8_QTYWpBnBkaam9GZXEy0yJTc"),
	CONTINUEVIP("continuevip", "续费成功通知", "N47AOCSzNGbzMpQKT5LoWbNVmxZw5oDea10IB5nW3H8"),
	UNBIND("unbind", "解绑成功通知", "b3oPt48IUTBghJJDew1HQ9kV46RU9Vpj26m8b49kXaY"),
	MAINTAIN("maintain", "维护完成通知", "EGTp1Q-_WHRyIRWtgHcW4Jkc93Jojhx5W6PhcaGSupo"),
	LEAVE("leave", "留言提交成功通知", "NHyt_uXJ9om6HZhCaBra6jgoH7sm9MCQSt0EjTKCAbY");

	TemplateMsgTypeEnum(String code, String des, String templateId) {
		this.code = code;
		this.des = des;
		this.templateId = templateId;
	}

	private String code;
	private String des;
	private String templateId;

	public String getCode() {
		return code;
	}

	public String getDes() {
		return des;
	}

	public String getTemplateId() {
		return templateId;
	}

}
