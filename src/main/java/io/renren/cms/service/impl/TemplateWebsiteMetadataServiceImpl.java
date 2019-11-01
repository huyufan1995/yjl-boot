package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.TemplateWebsiteMetadataDao;
import io.renren.cms.entity.TemplateWebsiteMetadataEntity;
import io.renren.cms.service.TemplateWebsiteMetadataService;

/**
 * 官网模板元数据服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateWebsiteMetadataService")
public class TemplateWebsiteMetadataServiceImpl implements TemplateWebsiteMetadataService {

	@Autowired
	private TemplateWebsiteMetadataDao templateWebsiteMetadataDao;

	@Override
	public TemplateWebsiteMetadataEntity queryObject(Integer id) {
		return templateWebsiteMetadataDao.queryObject(id);
	}

	@Override
	public List<TemplateWebsiteMetadataEntity> queryList(Map<String, Object> map) {
		return templateWebsiteMetadataDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateWebsiteMetadataDao.queryTotal(map);
	}

	@Override
	public void save(TemplateWebsiteMetadataEntity templateWebsiteMetadata) {
		templateWebsiteMetadataDao.save(templateWebsiteMetadata);
	}

	@Override
	public void update(TemplateWebsiteMetadataEntity templateWebsiteMetadata) {
		templateWebsiteMetadataDao.update(templateWebsiteMetadata);
	}

	@Override
	public void delete(Integer id) {
		templateWebsiteMetadataDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateWebsiteMetadataDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return templateWebsiteMetadataDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateWebsiteMetadataDao.logicDelBatch(ids);
	}

	@Override
	public TemplateWebsiteMetadataEntity queryObjectByMemberId(Integer memberId) {
		return templateWebsiteMetadataDao.queryObjectByMemberId(memberId);
	}

}
