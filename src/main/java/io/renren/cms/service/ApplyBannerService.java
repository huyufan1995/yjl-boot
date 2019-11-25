package io.renren.cms.service;

import io.renren.cms.entity.ApplyBannerEntity;

import java.util.List;
import java.util.Map;

/**
 * 接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-25 10:59:15
 */
public interface ApplyBannerService {
	
	ApplyBannerEntity queryObject(Integer id);
	
	List<ApplyBannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ApplyBannerEntity applyBanner);
	
	void update(ApplyBannerEntity applyBanner);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
