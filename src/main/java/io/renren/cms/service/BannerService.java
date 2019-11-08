package io.renren.cms.service;

import io.renren.cms.entity.BannerEntity;

import java.util.List;
import java.util.Map;

/**
 * banner
 *
 * @author moran
 * @date 2019-11-8
 */
public interface BannerService {
	
	BannerEntity queryObject(Integer id);
	
	List<BannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(BannerEntity banner);
	
	void update(BannerEntity banner);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	BannerEntity queryObjectByType(String type);
}
