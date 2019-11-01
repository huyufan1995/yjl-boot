package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.TemplateApplyEntity;
import io.renren.dao.BaseDao;

/**
 * 报名模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:45
 */
public interface TemplateApplyDao extends BaseDao<TemplateApplyEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
