package io.renren.cms.service.impl;

import io.renren.api.vo.CardAccessRecordEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.CardAccessRecordDao;
import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.cms.service.CardAccessRecordService;

/**
 * 名片访问记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("cardAccessRecordService")
public class CardAccessRecordServiceImpl implements CardAccessRecordService {

	@Autowired
	private CardAccessRecordDao cardAccessRecordDao;

	@Override
	public CardAccessRecordEntity queryObject(Integer id) {
		return cardAccessRecordDao.queryObject(id);
	}

	@Override
	public List<CardAccessRecordEntity> queryList(Map<String, Object> map) {
		return cardAccessRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return cardAccessRecordDao.queryTotal(map);
	}

	@Override
	public void save(CardAccessRecordEntity cardAccessRecord) {
		cardAccessRecordDao.save(cardAccessRecord);
	}

	@Override
	public void update(CardAccessRecordEntity cardAccessRecord) {
		cardAccessRecordDao.update(cardAccessRecord);
	}

	@Override
	public void delete(Integer id) {
		cardAccessRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		cardAccessRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return cardAccessRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return cardAccessRecordDao.logicDelBatch(ids);
	}

	@Override
	public CardAccessRecordEntity queryObjectByOpenidAndCardId(String openid, Integer cardId) {
		return cardAccessRecordDao.queryObjectByOpenidAndCardId(openid, cardId);
	}

	@Override
	public List<CardAccessRecordEntityVo> queryListVo(Map<String, Object> map) {
		return cardAccessRecordDao.queryListVo(map);
	}

}
