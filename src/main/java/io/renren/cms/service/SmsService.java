package io.renren.cms.service;

import java.util.ArrayList;

/**
 * 短信服务接口
 * 
 * @author yujia
 *
 */
public interface SmsService {

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 *            手机号
	 * @param templateId
	 *            短信模板ID
	 * @param args
	 *            其他参数
	 * @return
	 */
	boolean send(String mobile, Integer templateId, String smsSign, Object... args);

	boolean groupSend(ArrayList<String> mobiles, Integer templateId, String smsSign, Object... args);

}
