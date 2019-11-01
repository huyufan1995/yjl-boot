package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.SortDao;
import io.renren.cms.entity.SortEntity;
import io.renren.cms.service.SortService;



@Service("sortService")
public class SortServiceImpl implements SortService {
	@Autowired
	private SortDao sortDao;
	
	@Override
	public SortEntity queryObject(Integer id){
		return sortDao.queryObject(id);
	}
	
	@Override
	public List<SortEntity> queryList(Map<String, Object> map){
		return sortDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sortDao.queryTotal(map);
	}
	
	@Override
	public void save(SortEntity sort){
		sortDao.save(sort);
	}
	
	@Override
	public void update(SortEntity sort){
		sortDao.update(sort);
	}
	
	@Override
	public void delete(Integer id){
		sortDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		sortDao.deleteBatch(ids);
	}

	@Override
	public SortEntity queryObjectByType(String type) {
		return sortDao.queryObjectByType(type);
	}
	
}
