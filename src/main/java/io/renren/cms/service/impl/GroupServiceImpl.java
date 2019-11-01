package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.GroupDao;
import io.renren.cms.entity.GroupEntity;
import io.renren.cms.service.GroupService;



@Service("groupService")
public class GroupServiceImpl implements GroupService {
	@Autowired
	private GroupDao groupDao;
	
	@Override
	public GroupEntity queryObject(Integer id){
		return groupDao.queryObject(id);
	}
	
	@Override
	public List<GroupEntity> queryList(Map<String, Object> map){
		return groupDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return groupDao.queryTotal(map);
	}
	
	@Override
	public void save(GroupEntity group){
		groupDao.save(group);
	}
	
	@Override
	public void update(GroupEntity group){
		groupDao.update(group);
	}
	
	@Override
	public void delete(Integer id){
		groupDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		groupDao.deleteBatch(ids);
	}
	
}
