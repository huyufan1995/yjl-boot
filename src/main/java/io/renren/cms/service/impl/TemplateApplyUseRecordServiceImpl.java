package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.TemplateApplyUseRecordDao;
import io.renren.cms.entity.TemplateApplyUseRecordEntity;
import io.renren.cms.service.TemplateApplyUseRecordService;

/**
 * 报名模板使用记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateApplyUseRecordService")
public class TemplateApplyUseRecordServiceImpl implements TemplateApplyUseRecordService {

	@Autowired
	private TemplateApplyUseRecordDao templateApplyUseRecordDao;

	@Override
	public TemplateApplyUseRecordEntity queryObject(Integer id) {
		return templateApplyUseRecordDao.queryObject(id);
	}

	@Override
	public List<TemplateApplyUseRecordEntity> queryList(Map<String, Object> map) {
		return templateApplyUseRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateApplyUseRecordDao.queryTotal(map);
	}

	@Override
	public void save(TemplateApplyUseRecordEntity templateApplyUseRecord) {
		templateApplyUseRecordDao.save(templateApplyUseRecord);
	}

	@Override
	public void update(TemplateApplyUseRecordEntity templateApplyUseRecord) {
		templateApplyUseRecordDao.update(templateApplyUseRecord);
	}

	@Override
	public void delete(Integer id) {
		templateApplyUseRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateApplyUseRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return templateApplyUseRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateApplyUseRecordDao.logicDelBatch(ids);
	}

	@Override
	public List<TemplateApplyUseRecordEntity> queryListByTemplateApplyId(Integer templateApplyId) {
		return templateApplyUseRecordDao.queryListByTemplateApplyId(templateApplyId);
	}

}
