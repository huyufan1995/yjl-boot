package io.renren.cms.service;

import io.renren.cms.entity.CollectEntity;

import java.util.List;
import java.util.Map;

/**
 * 收藏接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 17:25:18
 */
public interface CollectService {
	
	CollectEntity queryObject(Integer id);
	
	List<CollectEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CollectEntity collect);
	
	void update(CollectEntity collect);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	Boolean isCollect(Integer id, Integer collectType, String openid);
}
