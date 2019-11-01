package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.dao.BaseDao;

/**
 * 模板项
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface TemplateItmeDao extends BaseDao<TemplateItmeEntity> {

	List<TemplateItmeEntity> queryListByTemplateId(Integer templateId);

}
