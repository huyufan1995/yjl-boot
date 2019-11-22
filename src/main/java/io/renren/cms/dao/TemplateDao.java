package io.renren.cms.dao;

import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * 模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface TemplateDao extends BaseDao<TemplateEntity> {

	int releaseBatch(Object[] id);

	int cancelReleaseBatch(Object[] id);

	List<TemplateEntity> queryListApi(Map<String, Object> map);

	List<TemplateEntity> queryHotListApi(Map<String, Object> map);

	List<TemplateEntity> queryPraiseListApi(Map<String, Object> map);

	TemplateEntity queryPraiseInfo(Map<String, Object> map);

	List<TemplateEntity> queryListIndex();

	List<TemplateItmeEntity> queryListByTemplateId(Integer templateId);
}
