package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.TemplateWebsiteMetadataEntity;
import io.renren.dao.BaseDao;

/**
 * 官网模板元数据
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 16:59:43
 */
public interface TemplateWebsiteMetadataDao extends BaseDao<TemplateWebsiteMetadataEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteMetadataEntity queryObjectByMemberId(Integer memberId);
}
