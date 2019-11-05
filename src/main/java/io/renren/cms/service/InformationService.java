package io.renren.cms.service;

import io.renren.cms.entity.InformationsEntity;

import java.util.List;
import java.util.Map;

/**
 * 接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 10:36:31
 */
public interface InformationService {
	
	InformationsEntity queryObject(Integer id);
	
	List<InformationsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InformationsEntity information);
	
	void update(InformationsEntity information);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
