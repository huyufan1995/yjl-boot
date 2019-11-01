package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.CardHolderAccessRecordDao;
import io.renren.cms.entity.CardHolderAccessRecordEntity;
import io.renren.cms.service.CardHolderAccessRecordService;

/**
 * 名片夹访问记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("cardHolderAccessRecordService")
public class CardHolderAccessRecordServiceImpl implements CardHolderAccessRecordService {

	@Autowired
	private CardHolderAccessRecordDao cardHolderAccessRecordDao;

	@Override
	public CardHolderAccessRecordEntity queryObject(Integer id) {
		return cardHolderAccessRecordDao.queryObject(id);
	}

	@Override
	public List<CardHolderAccessRecordEntity> queryList(Map<String, Object> map) {
		return cardHolderAccessRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return cardHolderAccessRecordDao.queryTotal(map);
	}

	@Override
	public void save(CardHolderAccessRecordEntity cardHolderAccessRecord) {
		cardHolderAccessRecordDao.save(cardHolderAccessRecord);
	}

	@Override
	public void update(CardHolderAccessRecordEntity cardHolderAccessRecord) {
		cardHolderAccessRecordDao.update(cardHolderAccessRecord);
	}

	@Override
	public void delete(Integer id) {
		cardHolderAccessRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		cardHolderAccessRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return cardHolderAccessRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return cardHolderAccessRecordDao.logicDelBatch(ids);
	}

	@Override
	public CardHolderAccessRecordEntity queryObjectByOpenidAndCardHolderId(Integer cardHolderId, String openid) {
		return cardHolderAccessRecordDao.queryObjectByOpenidAndCardHolderId(cardHolderId, openid);
	}

}
