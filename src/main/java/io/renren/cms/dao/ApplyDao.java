package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.ApplyEntity;
import io.renren.dao.BaseDao;

/**
 * 活动
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 19:02:39
 */
public interface ApplyDao extends BaseDao<ApplyEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
