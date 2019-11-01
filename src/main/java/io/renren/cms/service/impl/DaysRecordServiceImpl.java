package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.DaysRecordDao;
import io.renren.cms.entity.DaysRecordEntity;
import io.renren.cms.service.DaysRecordService;



@Service("daysRecordService")
public class DaysRecordServiceImpl implements DaysRecordService {
	@Autowired
	private DaysRecordDao daysRecordDao;
	
	@Override
	public DaysRecordEntity queryObject(Integer id){
		return daysRecordDao.queryObject(id);
	}
	
	@Override
	public List<DaysRecordEntity> queryList(Map<String, Object> map){
		return daysRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return daysRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(DaysRecordEntity daysRecord){
		daysRecordDao.save(daysRecord);
	}
	
	@Override
	public void update(DaysRecordEntity daysRecord){
		daysRecordDao.update(daysRecord);
	}
	
	@Override
	public void delete(Integer id){
		daysRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		daysRecordDao.deleteBatch(ids);
	}
	
}
