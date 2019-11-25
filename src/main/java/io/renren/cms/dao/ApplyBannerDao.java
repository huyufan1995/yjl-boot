package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.ApplyBannerEntity;
import io.renren.dao.BaseDao;

/**
 * 
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-25 10:59:15
 */
public interface ApplyBannerDao extends BaseDao<ApplyBannerEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
