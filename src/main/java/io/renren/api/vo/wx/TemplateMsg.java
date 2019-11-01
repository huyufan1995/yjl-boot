package io.renren.api.vo.wx;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateMsg {

	private String touser; // 接收者（用户）的 openid *
	private String template_id; // 所需下发的模板消息的id *
	private String page; // 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
	private String form_id; // 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id *
	private Map<Object, Object> data; // 模板内容，不填则下发空模板 *

	private String emphasis_keyword;// 模板需要放大的关键词，不填则默认无放大 keyword1.DATA

}
