package io.renren.cms.service.impl;

import io.renren.api.vo.CardHolderEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.CardHolderDao;
import io.renren.cms.entity.CardHolderEntity;
import io.renren.cms.service.CardHolderService;

/**
 * 名片夹服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("cardHolderService")
public class CardHolderServiceImpl implements CardHolderService {

	@Autowired
	private CardHolderDao cardHolderDao;
	
	@Override
	public CardHolderEntity queryObject(Integer id){
		return cardHolderDao.queryObject(id);
	}
	
	@Override
	public List<CardHolderEntity> queryList(Map<String, Object> map){
		return cardHolderDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return cardHolderDao.queryTotal(map);
	}
	
	@Override
	public void save(CardHolderEntity cardHolder){
		cardHolderDao.save(cardHolder);
	}
	
	@Override
	public void update(CardHolderEntity cardHolder){
		cardHolderDao.update(cardHolder);
	}
	
	@Override
	public void delete(Integer id){
		cardHolderDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		cardHolderDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return cardHolderDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return cardHolderDao.logicDelBatch(ids);
	}

	@Override
	public List<CardHolderEntityVo> queryListVo(Map<String, Object> map) {
		return cardHolderDao.queryListVo(map);
	}


}
