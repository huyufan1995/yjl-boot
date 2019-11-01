package io.renren.cms.service;

import io.renren.cms.entity.TemplateWebsiteMetadataEntity;

import java.util.List;
import java.util.Map;

/**
 * 官网模板元数据接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 16:59:43
 */
public interface TemplateWebsiteMetadataService {

	TemplateWebsiteMetadataEntity queryObject(Integer id);

	List<TemplateWebsiteMetadataEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TemplateWebsiteMetadataEntity templateWebsiteMetadata);

	void update(TemplateWebsiteMetadataEntity templateWebsiteMetadata);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteMetadataEntity queryObjectByMemberId(Integer memberId);
}
