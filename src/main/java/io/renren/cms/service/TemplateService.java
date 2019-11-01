package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;

/**
 * 模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface TemplateService {

	TemplateEntity queryObject(Integer id);

	List<TemplateEntity> queryList(Map<String, Object> map);

	List<TemplateEntity> queryListApi(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TemplateEntity template);

	void update(TemplateEntity template);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	void releaseBatch(Object[] id);

	void cancelReleaseBatch(Object[] id);

	List<TemplateEntity> queryHotListApi(Map<String, Object> map);

	List<TemplateEntity> queryPraiseListApi(Map<String, Object> map);

	TemplateEntity queryPraiseInfo(Map<String, Object> map);

	List<TemplateEntity> queryListIndex();

	List<TemplateItmeEntity> queryListByTemplateId(Integer templateId);

}
