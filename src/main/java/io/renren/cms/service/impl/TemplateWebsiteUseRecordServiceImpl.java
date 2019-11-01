package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteUseRecordEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.TemplateWebsiteUseRecordDao;
import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import io.renren.cms.service.TemplateWebsiteUseRecordService;

/**
 * 官网模板使用记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateWebsiteUseRecordService")
public class TemplateWebsiteUseRecordServiceImpl implements TemplateWebsiteUseRecordService {

	@Autowired
	private TemplateWebsiteUseRecordDao templateWebsiteUseRecordDao;

	@Override
	public TemplateWebsiteUseRecordEntity queryObject(Integer id) {
		return templateWebsiteUseRecordDao.queryObject(id);
	}

	@Override
	public List<TemplateWebsiteUseRecordEntity> queryList(Map<String, Object> map) {
		return templateWebsiteUseRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateWebsiteUseRecordDao.queryTotal(map);
	}

	@Override
	public void save(TemplateWebsiteUseRecordEntity templateWebsiteUseRecord) {
		templateWebsiteUseRecordDao.save(templateWebsiteUseRecord);
	}

	@Override
	public void update(TemplateWebsiteUseRecordEntity templateWebsiteUseRecord) {
		templateWebsiteUseRecordDao.update(templateWebsiteUseRecord);
	}

	@Override
	public void delete(Integer id) {
		templateWebsiteUseRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateWebsiteUseRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return templateWebsiteUseRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateWebsiteUseRecordDao.logicDelBatch(ids);
	}

	@Override
	public TemplateWebsiteUseRecordEntity queryObjectByTemplateWebsiteIdAndMemberId(Integer templateWebsiteId,
			Integer memberId) {
		return templateWebsiteUseRecordDao.queryObjectByTemplateWebsiteIdAndMemberId(templateWebsiteId, memberId);
	}

	@Override
	public List<TemplateWebsiteUseRecordEntity> queryListByTemplateWebsiteId(Integer templateWebsiteId) {
		return templateWebsiteUseRecordDao.queryListByTemplateWebsiteId(templateWebsiteId);
	}

	@Override
	public List<TemplateWebsiteUseRecordEntityVo> queryListVo(Map<String, Object> map) {
		return templateWebsiteUseRecordDao.queryListVo(map);
	}

}
