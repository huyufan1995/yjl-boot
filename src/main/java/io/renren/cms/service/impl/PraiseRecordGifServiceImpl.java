package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.PraiseRecordGifDao;
import io.renren.cms.entity.PraiseRecordGifEntity;
import io.renren.cms.service.PraiseRecordGifService;

@Service("praiseRecordGifService")
public class PraiseRecordGifServiceImpl implements PraiseRecordGifService {
	@Autowired
	private PraiseRecordGifDao praiseRecordGifDao;

	@Override
	public PraiseRecordGifEntity queryObject(Integer id) {
		return praiseRecordGifDao.queryObject(id);
	}

	@Override
	public List<PraiseRecordGifEntity> queryList(Map<String, Object> map) {
		return praiseRecordGifDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return praiseRecordGifDao.queryTotal(map);
	}

	@Override
	public void save(PraiseRecordGifEntity praiseRecordGif) {
		praiseRecordGifDao.save(praiseRecordGif);
	}

	@Override
	public void update(PraiseRecordGifEntity praiseRecordGif) {
		praiseRecordGifDao.update(praiseRecordGif);
	}

	@Override
	public void delete(Integer id) {
		praiseRecordGifDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		praiseRecordGifDao.deleteBatch(ids);
	}

	@Override
	public PraiseRecordGifEntity queryObjectByTemplateIdAndOpenId(String openId, Integer templateId) {
		return praiseRecordGifDao.queryObjectByTemplateIdAndOpenId(openId, templateId);
	}

}
