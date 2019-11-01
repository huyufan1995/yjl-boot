package io.renren.cms.service;

import io.renren.api.vo.TemplateItmesForm;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.entity.UseRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 模板项
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface TemplateItmeService {
	
	TemplateItmeEntity queryObject(Integer id);
	
	List<TemplateItmeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TemplateItmeEntity templateItme);
	
	void update(TemplateItmeEntity templateItme);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	List<TemplateItmeEntity> queryListByTemplateId(Integer templateId);

	UseRecordEntity use(TemplateItmesForm templateItmesForm);
}
