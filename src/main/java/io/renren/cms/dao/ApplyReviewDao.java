package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.ApplyReviewEntity;
import io.renren.dao.BaseDao;

/**
 * 活动回顾
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 17:31:14
 */
public interface ApplyReviewDao extends BaseDao<ApplyReviewEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
