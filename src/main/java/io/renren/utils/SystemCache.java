package io.renren.utils;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;

public class SystemCache {

	/** 短信验证码缓存 */
	public static Cache<String, String> smsCodeCache = CacheUtil.newFIFOCache(128, DateUnit.MINUTE.getMillis() * 5);

}
