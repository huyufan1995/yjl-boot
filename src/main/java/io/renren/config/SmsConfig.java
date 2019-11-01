package io.renren.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qcloud.sms.SmsMultiSender;
import com.qcloud.sms.SmsSingleSender;

/**
 * 腾讯短信配置
 * 
 * @author yujia
 *
 */
@Configuration
public class SmsConfig {

	@Value("${tencent.sms.appId}")
	private Integer appId;
	@Value("${tencent.sms.appKey}")
	private String appKey;

	// 单发
	@Bean
	public SmsSingleSender smsSingleSender() {
		try {
			SmsSingleSender singleSender = new SmsSingleSender(appId, appKey);
			return singleSender;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 群发
	@Bean
	public SmsMultiSender smsMultiSender() {
		try {
			SmsMultiSender smsMultiSender = new SmsMultiSender(appId, appKey);
			return smsMultiSender;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
