package io.renren.cms.service;

import io.renren.cms.entity.GroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:45
 */
public interface GroupService {
	
	GroupEntity queryObject(Integer id);
	
	List<GroupEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GroupEntity group);
	
	void update(GroupEntity group);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
