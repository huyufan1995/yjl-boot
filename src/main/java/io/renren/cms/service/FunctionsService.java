package io.renren.cms.service;

import io.renren.cms.entity.FunctionsEntity;

import java.util.List;
import java.util.Map;

/**
 * 功能权益接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-30 15:17:45
 */
public interface FunctionsService {
	
	FunctionsEntity queryObject(Integer id);
	
	List<FunctionsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(FunctionsEntity functions);
	
	void update(FunctionsEntity functions);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
