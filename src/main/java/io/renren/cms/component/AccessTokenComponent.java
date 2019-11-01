package io.renren.cms.component;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.renren.properties.YykjProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenComponent {
	
	public static final String CACHE_KEY_ACCESS_TOKEN = "access_token";
	public static final String API_REFRESH_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}";
	
	@Autowired
	private YykjProperties yykjProperties;

	private Cache<String, Object> cache = CacheBuilder.newBuilder()
			// 最大缓存1个
			.maximumSize(1)
			// 设置写缓存后 6600 秒钟过期
			.expireAfterWrite(6600, TimeUnit.SECONDS).build();
	
	
	public String getAccessToken() {
		Object accessTokenCache = cache.getIfPresent(CACHE_KEY_ACCESS_TOKEN);
		if(null == accessTokenCache) {
			cache.put(CACHE_KEY_ACCESS_TOKEN, refreshAccessToken());
		}
		return cache.getIfPresent(CACHE_KEY_ACCESS_TOKEN).toString();
	}
	
	public String refreshAccessToken() {
		String result = HttpUtil
				.get(StrUtil.format(API_REFRESH_ACCESS_TOKEN, yykjProperties.getAppid(), yykjProperties.getSecret()));
		log.info("刷新微信TOKEN:{}", result);
		JSONObject jo = JSON.parseObject(result);
		return jo.getString("access_token");
	}

}
