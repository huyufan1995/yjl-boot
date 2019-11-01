package io.renren.cms.service;

import io.renren.cms.entity.UserDaysEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
public interface UserDaysService {
	
	UserDaysEntity queryObject(Integer id);
	
	List<UserDaysEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserDaysEntity userDays);
	
	void update(UserDaysEntity userDays);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	UserDaysEntity queryObjectByParam(Map<String, Object> map);

	UserDaysEntity dealUserDays(String openId, String type);

	void dealOldUserDays(String openid, String type);
}
