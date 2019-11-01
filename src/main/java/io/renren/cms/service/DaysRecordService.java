package io.renren.cms.service;

import io.renren.cms.entity.DaysRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
public interface DaysRecordService {
	
	DaysRecordEntity queryObject(Integer id);
	
	List<DaysRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DaysRecordEntity daysRecord);
	
	void update(DaysRecordEntity daysRecord);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
