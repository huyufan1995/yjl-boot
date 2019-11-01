package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.UseRuleDao;
import io.renren.cms.entity.UseRuleEntity;
import io.renren.cms.service.UseRuleService;



@Service("useRuleService")
public class UseRuleServiceImpl implements UseRuleService {
	@Autowired
	private UseRuleDao useRuleDao;
	
	@Override
	public UseRuleEntity queryObject(Integer id){
		return useRuleDao.queryObject(id);
	}
	
	@Override
	public List<UseRuleEntity> queryList(Map<String, Object> map){
		return useRuleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return useRuleDao.queryTotal(map);
	}
	
	@Override
	public void save(UseRuleEntity useRule){
		useRuleDao.save(useRule);
	}
	
	@Override
	public void update(UseRuleEntity useRule){
		useRuleDao.update(useRule);
	}
	
	@Override
	public void delete(Integer id){
		useRuleDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		useRuleDao.deleteBatch(ids);
	}

	@Override
	public UseRuleEntity queryObjectByType(String type) {
		return useRuleDao.queryObjectByType(type);
	}
	
}
