package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.SubscibeEntity;

/**
 * 订阅接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-06-28 17:06:27
 */
public interface SubscibeService {

	SubscibeEntity queryObject(Integer id);

	List<SubscibeEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(SubscibeEntity subscibe);

	void update(SubscibeEntity subscibe);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	SubscibeEntity queryObjectByOpenid(String openid);

	List<SubscibeEntity> queryListByStatus();

	int deleteByFormid(String formid);

	List<SubscibeEntity> queryListByPlatformGroup(String platform);

}
