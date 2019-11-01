package io.renren.cms.service;

import io.renren.cms.entity.GroupRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 转发记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:36
 */
public interface GroupRecordService {
	
	GroupRecordEntity queryObject(Integer id);
	
	List<GroupRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GroupRecordEntity groupRecord);
	
	void update(GroupRecordEntity groupRecord);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	List<GroupRecordEntity> queryObjectByParam(Map<String, Object> map);
}
