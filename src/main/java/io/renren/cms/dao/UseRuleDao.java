package io.renren.cms.dao;

import io.renren.cms.entity.UseRuleEntity;
import io.renren.dao.BaseDao;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
public interface UseRuleDao extends BaseDao<UseRuleEntity> {

	UseRuleEntity queryObjectByType(String type);
	
}
