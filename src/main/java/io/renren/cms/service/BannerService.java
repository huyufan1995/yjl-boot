package io.renren.cms.service;

import io.renren.cms.entity.BannerEntity;

import java.util.List;
import java.util.Map;

/**
 * banner
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-24 12:13:18
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
