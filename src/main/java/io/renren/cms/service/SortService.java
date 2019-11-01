package io.renren.cms.service;

import io.renren.cms.entity.SortEntity;

import java.util.List;
import java.util.Map;

/**
 * 首页数据天数
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:37
 */
public interface SortService {
	
	SortEntity queryObject(Integer id);
	
	List<SortEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SortEntity sort);
	
	void update(SortEntity sort);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	SortEntity queryObjectByType(String type);
}
