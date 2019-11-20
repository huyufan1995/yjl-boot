package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.ApplyVerifyEntity;
import io.renren.dao.BaseDao;

/**
 * 活动核销人员配置
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:36
 */
public interface ApplyVerifyDao extends BaseDao<ApplyVerifyEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
