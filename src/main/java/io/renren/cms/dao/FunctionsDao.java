package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.FunctionsEntity;
import io.renren.dao.BaseDao;

/**
 * 功能权益
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-30 15:17:45
 */
public interface FunctionsDao extends BaseDao<FunctionsEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
