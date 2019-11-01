package io.renren.cms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.dao.PraiseRecordDao;
import io.renren.cms.entity.PraiseRecordEntity;
import io.renren.cms.service.PraiseRecordService;



@Service("praiseRecordService")
public class PraiseRecordServiceImpl implements PraiseRecordService {
	@Autowired
	private PraiseRecordDao praiseRecordDao;
	
	@Override
	public PraiseRecordEntity queryObject(Integer id){
		return praiseRecordDao.queryObject(id);
	}
	
	@Override
	public List<PraiseRecordEntity> queryList(Map<String, Object> map){
		return praiseRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return praiseRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(PraiseRecordEntity praiseRecord){
		praiseRecordDao.save(praiseRecord);
	}
	
	@Override
	public void update(PraiseRecordEntity praiseRecord){
		praiseRecordDao.update(praiseRecord);
	}
	
	@Override
	public void delete(Integer id){
		praiseRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		praiseRecordDao.deleteBatch(ids);
	}

	@Override
	public List<PraiseRecordEntity> queryPraiseList(Map<String, Object> map) {
		return praiseRecordDao.queryPraiseList(map);
	}

	@Override
	public void changePraiseStatus(PraiseRecordEntity praiseRecord) {
		praiseRecordDao.changePraiseStatus(praiseRecord);
	}

	@Override
	public PraiseRecordEntity queryObjectByMap(Map<String, Object> params) {
		return praiseRecordDao.queryObjectByMap(params);
	}

	@Override
	public boolean ifPraise(Map<String, Object> params) {
		PraiseRecordEntity praise = praiseRecordDao.queryObjectByMap(params);
		if(null!=praise&&StringUtils.equals(praise.getPraiseStatus(), SystemConstant.PRAISE_SUCCESS)){
			return true;
		}else{
			return false;
		}
	}
	
}
