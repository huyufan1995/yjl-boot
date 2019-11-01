package io.renren.api.kit;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.renren.api.kit.crypto.PKCS7Encoder;
import io.renren.api.vo.OpenIdClass;
import io.renren.api.vo.wx.TemplateMsg;
import io.renren.api.vo.wx.WxResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeixinAppKit {

	public static final String URL_JSCODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid={}&secret={}&js_code={}&grant_type=authorization_code";
	// 发送模版消息
	public static final String SEND_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token={}";
	
	public static final String APP_PAGE_INDEX = "pages/index/index";
	// 定制模板消息ID
	public static final String APP_TEMPLATE_ID_CUSTOMIZATION = "bUlXZLKUUE-vIE4V675KXHDPLQzUiWlVut_SubUs6Lw";
	public static final String APP_TEMPLATE_ID_WALLPAPER = "v2HvEj-lnN9ZPUAQPTg4_QoeRQsW5vMgf7uQRmldp2A";
	
	/**
	 * 发送模板消息
	 * 
	 * @param accessToken
	 * @param formId
	 * @param page
	 * @param templateId
	 * @param openId
	 * @param data
	 * @return
	 */
	public static WxResult sendTemplate(String accessToken, String formId, String page, String templateId,
			String openId, Map<Object, Object> data) {
		TemplateMsg templateMsg = new TemplateMsg();
		templateMsg.setData(data);
		templateMsg.setForm_id(formId);
		templateMsg.setPage(page);
		templateMsg.setTemplate_id(templateId);
		templateMsg.setTouser(openId);
//		templateMsg.setEmphasis_keyword("keyword1.DATA");
		log.info("发送模板消息参数:{}", JSON.toJSONString(templateMsg));
		String result = HttpUtil.post(StrUtil.format(SEND_TEMPLATE, accessToken), JSON.toJSONString(templateMsg));
		log.info("发送模板消息结果:{}", result);
		return JSON.parseObject(result, WxResult.class);
	}

	public static OpenIdClass getOpenIdClass(String code, String appId, String appSecret) {
		log.info("code==>" + code);
		String url = StrUtil.format(URL_JSCODE2SESSION, appId, appSecret, code);
		String result = HttpUtil.get(url);
		log.info("获取openid:" + result);
		OpenIdClass openIdClass = JSON.parseObject(result, OpenIdClass.class);
		return openIdClass;
	}

	/**
	 * 检测签名
	 * 
	 */
	public static boolean checkUserInfo(String sessionKey, String rawData, String signature) {
		final String generatedSignature = DigestUtils.sha1Hex(rawData + sessionKey);
		log.info("generatedSignature==>" + generatedSignature);
		return generatedSignature.equals(signature);
	}

	/**
	 * AES解密
	 *
	 * @param encryptedData
	 *            消息密文
	 * @param ivStr
	 *            iv字符串
	 */
	public static String decrypt(String sessionKey, String encryptedData, String ivStr) {
		try {
			AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
			params.init(new IvParameterSpec(Base64.decodeBase64(ivStr)));

			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES"), params);

			return new String(PKCS7Encoder.decode(cipher.doFinal(Base64.decodeBase64(encryptedData))),
					StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("AES解密失败", e);
		}
	}

}
