package io.renren.cms.service;

import io.renren.cms.entity.TemplateApplyEntity;

import java.util.List;
import java.util.Map;

/**
 * 报名模板接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:45
 */
public interface TemplateApplyService {
	
	TemplateApplyEntity queryObject(Integer id);
	
	List<TemplateApplyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TemplateApplyEntity templateApply);
	
	void update(TemplateApplyEntity templateApply);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
