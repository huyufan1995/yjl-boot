package io.renren.cms.dao;

import io.renren.cms.entity.BannerEntity;
import io.renren.dao.BaseDao;

/**
 * banner
 * 
 * @author moran
 * @date 2019-11-8
 */
public interface BannerDao extends BaseDao<BannerEntity> {

	BannerEntity queryObjectByType(String type);
	
}
