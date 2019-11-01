package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.GroupRecordDao;
import io.renren.cms.entity.GroupRecordEntity;
import io.renren.cms.service.GroupRecordService;



@Service("groupRecordService")
public class GroupRecordServiceImpl implements GroupRecordService {
	@Autowired
	private GroupRecordDao groupRecordDao;
	
	@Override
	public GroupRecordEntity queryObject(Integer id){
		return groupRecordDao.queryObject(id);
	}
	
	@Override
	public List<GroupRecordEntity> queryList(Map<String, Object> map){
		return groupRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return groupRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(GroupRecordEntity groupRecord){
		groupRecordDao.save(groupRecord);
	}
	
	@Override
	public void update(GroupRecordEntity groupRecord){
		groupRecordDao.update(groupRecord);
	}
	
	@Override
	public void delete(Integer id){
		groupRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		groupRecordDao.deleteBatch(ids);
	}

	@Override
	public List<GroupRecordEntity> queryObjectByParam(Map<String, Object> map) {
		return groupRecordDao.queryObjectByParam(map);
	}
	
}
