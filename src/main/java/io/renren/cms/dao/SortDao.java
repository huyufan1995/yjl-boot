package io.renren.cms.dao;

import io.renren.cms.entity.SortEntity;
import io.renren.dao.BaseDao;

/**
 * 首页数据天数
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:37
 */
public interface SortDao extends BaseDao<SortEntity> {

	SortEntity queryObjectByType(String type);
	
}
