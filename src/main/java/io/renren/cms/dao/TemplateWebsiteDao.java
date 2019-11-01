package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.TemplateWebsiteEntity;
import io.renren.dao.BaseDao;

/**
 * 官网模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
public interface TemplateWebsiteDao extends BaseDao<TemplateWebsiteEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    TemplateWebsiteEntity queryByLayoutWithCtime();
}
