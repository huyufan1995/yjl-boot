package io.renren.cms.service;

import io.renren.cms.entity.UseRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
public interface UseRuleService {
	
	UseRuleEntity queryObject(Integer id);
	
	List<UseRuleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UseRuleEntity useRule);
	
	void update(UseRuleEntity useRule);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	UseRuleEntity queryObjectByType(String type);
}
