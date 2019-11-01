package io.renren.cms.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcloud.sms.SmsMultiSender;
import com.qcloud.sms.SmsMultiSenderResult;
import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;

import io.renren.cms.service.SmsService;
import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯短信服务实现
 * 
 * @author yujia
 *
 */
@Slf4j
@Service("tencentSmsService")
public class TencentSmsServiceImpl implements SmsService {

	@Autowired
	private SmsSingleSender smsSingleSender;
	@Autowired
	private SmsMultiSender smsMultiSender;

	@Override
	public boolean send(String mobile, Integer templateId, String smsSign, Object... args) {
		try {
			ArrayList<String> params = new ArrayList<>(args.length);
			for (Object arg : args) {
				params.add(arg.toString());
			}
			SmsSingleSenderResult singleSenderResult = smsSingleSender.sendWithParam("86", mobile, templateId, params,
					smsSign, "", "");

			if (singleSenderResult.result == 0) {
				log.info("腾讯短信发送成功，手机号：{}", mobile);
				return true;
			} else {
				log.warn("腾讯短信发送失败，手机号：{},错误消息:{}", mobile, singleSenderResult.errMsg);
				return false;
			}
		} catch (Exception e) {
			log.error("腾讯发送短信异常：{}", e);
		}
		return false;
	}

	@Override
	public boolean groupSend(ArrayList<String> mobiles, Integer templateId, String smsSign, Object... args) {
		try {
			ArrayList<String> params = new ArrayList<>(args.length);
			for (Object arg : args) {
				params.add(arg.toString());
			}

			SmsMultiSenderResult smsMultiSenderResult = smsMultiSender.sendWithParam("86", mobiles, templateId, params,
					smsSign, "", ""); // 签名参数未提供或者为空时，会使用默认签名发送短信

			if (smsMultiSenderResult.result == 0) {
				log.info("腾讯群发短信发送成功，手机号：{}", mobiles);
				return true;
			} else {
				log.warn("腾讯群发短信发送失败，手机号：{}, 错误消息:{}", mobiles, smsMultiSenderResult.errMsg);
				return false;
			}
		} catch (Exception e) {
			log.error("腾讯群发发送短信异常：{}", e);
		}
		return false;
	}

}
