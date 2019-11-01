package io.renren.cms.service;

import io.renren.cms.entity.WxUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 微信用户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface WxUserService {
	
	WxUserEntity queryObject(Long id);
	
	List<WxUserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(WxUserEntity wxUser);
	
	void update(WxUserEntity wxUser);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
	
	WxUserEntity queryByOpenId(String openId);
}
