package io.renren.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;

import cn.xsshome.taip.ocr.TAipOcr;

@Configuration
public class TencentConfig {

	@Value("${tencent.cos.appId:#{null}}")
	private String cosAppId;
	@Value("${tencent.cos.secretId:#{null}}")
	private String cosSecretId;
	@Value("${tencent.cos.secretKey:#{null}}")
	private String cosSecretKey;
	@Value("${tencent.cos.region:#{null}}")
	private String cosRegion;

	@Value("${tencent.ai.appId:#{null}}")
	private String aiAppId;
	@Value("${tencent.ai.appKey:#{null}}")
	private String aiAppKey;

	@Bean(destroyMethod = "shutdown")
	public COSClient cosClient() {
		// 1 初始化用户身份信息(secretId, secretKey)
		COSCredentials cred = new BasicCOSCredentials(cosSecretId, cosSecretKey);
		// 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
		ClientConfig clientConfig = new ClientConfig(new Region(cosRegion));
		// 3 生成cos客户端
		COSClient cosclient = new COSClient(cred, clientConfig);
		return cosclient;
	}

	/**
	 * TAipOcr -文字识别
	 */
	@Bean
	public TAipOcr tAipOcr() {
		TAipOcr tAipOcr = new TAipOcr(aiAppId, aiAppKey);
		return tAipOcr;
	}

}
