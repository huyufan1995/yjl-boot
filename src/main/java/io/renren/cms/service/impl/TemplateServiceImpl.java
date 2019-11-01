package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.TemplateDao;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.service.TemplateService;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {
	@Autowired
	private TemplateDao templateDao;

	@Override
	public TemplateEntity queryObject(Integer id) {
		return templateDao.queryObject(id);
	}

	@Override
	public List<TemplateEntity> queryList(Map<String, Object> map) {
		return templateDao.queryList(map);
	}

	@Override
	public List<TemplateEntity> queryListApi(Map<String, Object> map) {
		return templateDao.queryListApi(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateDao.queryTotal(map);
	}

	@Override
	public void save(TemplateEntity template) {
		templateDao.save(template);
	}

	@Override
	public void update(TemplateEntity template) {
		templateDao.update(template);
	}

	@Override
	public void delete(Integer id) {
		templateDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateDao.deleteBatch(ids);
	}

	@Override
	public void releaseBatch(Object[] id) {
		templateDao.releaseBatch(id);
	}

	@Override
	public void cancelReleaseBatch(Object[] id) {
		templateDao.cancelReleaseBatch(id);
	}

	@Override
	public List<TemplateEntity> queryHotListApi(Map<String, Object> map) {
		return templateDao.queryHotListApi(map);
	}

	@Override
	public List<TemplateEntity> queryPraiseListApi(Map<String, Object> map) {
		return templateDao.queryPraiseListApi(map);
	}

	@Override
	public TemplateEntity queryPraiseInfo(Map<String, Object> map) {
		return templateDao.queryPraiseInfo(map);
	}

	@Override
	public List<TemplateEntity> queryListIndex() {
		return templateDao.queryListIndex();
	}

	@Override
	public List<TemplateItmeEntity> queryListByTemplateId(Integer templateId) {
		return templateDao.queryListByTemplateId(templateId);
	}

}
