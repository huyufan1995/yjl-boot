package io.renren.cms.dao;

import io.renren.cms.entity.BannerEntity;
import io.renren.dao.BaseDao;

/**
 * banner
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-24 12:13:18
 */
public interface BannerDao extends BaseDao<BannerEntity> {

	BannerEntity queryObjectByType(String type);
	
}
