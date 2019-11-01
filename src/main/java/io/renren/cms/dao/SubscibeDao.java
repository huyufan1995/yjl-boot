package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.SubscibeEntity;
import io.renren.dao.BaseDao;

/**
 * 订阅
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-06-28 17:06:27
 */
public interface SubscibeDao extends BaseDao<SubscibeEntity> {

	SubscibeEntity queryObjectByOpenid(String openid);

	List<SubscibeEntity> queryListByStatus();

	List<SubscibeEntity> queryListByPlatformGroup(String platform);

	int deleteByFormid(String formid);

}
