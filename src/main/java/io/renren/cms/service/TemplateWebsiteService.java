package io.renren.cms.service;

import io.renren.cms.entity.TemplateWebsiteEntity;

import java.util.List;
import java.util.Map;

/**
 * 官网模板接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
public interface TemplateWebsiteService {
	
	TemplateWebsiteEntity queryObject(Integer id);

	TemplateWebsiteEntity queryByLayoutWithCtime();

	List<TemplateWebsiteEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TemplateWebsiteEntity templateWebsite);
	
	void update(TemplateWebsiteEntity templateWebsite);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
