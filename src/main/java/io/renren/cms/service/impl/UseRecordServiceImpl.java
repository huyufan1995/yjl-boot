package io.renren.cms.service.impl;

import io.renren.cms.dao.UseRecordDao;
import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.service.UseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("useRecordService")
public class UseRecordServiceImpl implements UseRecordService {
	@Autowired
	private UseRecordDao useRecordDao;

	@Override
	public UseRecordEntity queryObject(Integer id) {
		return useRecordDao.queryObject(id);
	}

	@Override
	public List<UseRecordEntity> queryList(Map<String, Object> map) {
		return useRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return useRecordDao.queryTotal(map);
	}

	@Override
	public void save(UseRecordEntity useRecord) {
		useRecordDao.save(useRecord);
	}

	@Override
	public void update(UseRecordEntity useRecord) {
		useRecordDao.update(useRecord);
	}

	@Override
	public void delete(Integer id) {
		useRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		useRecordDao.deleteBatch(ids);
	}

	@Override
	public List<UseRecordEntity> queryListByTemplateId(Integer templateId) {
		return useRecordDao.queryListByTemplateId(templateId);
	}

	@Override
	public List<UseRecordEntity> queryListByGroup(Map<String, Object> map) {
		return useRecordDao.queryListByGroup(map);
	}

	@Override
	public List<UseRecordEntity> queryListByTemplateIdGroup(Integer templateId) {
		return useRecordDao.queryListByTemplateIdGroup(templateId);
	}

}
