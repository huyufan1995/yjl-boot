package io.renren.cms.dao;

import java.util.Map;

import io.renren.cms.entity.UserDaysEntity;
import io.renren.dao.BaseDao;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
public interface UserDaysDao extends BaseDao<UserDaysEntity> {

	UserDaysEntity queryObjectByParam(Map<String, Object> map);
	
}
