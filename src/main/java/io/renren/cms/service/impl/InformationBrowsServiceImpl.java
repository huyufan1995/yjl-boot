package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.InformationBrowsDao;
import io.renren.cms.entity.InformationBrowsEntity;
import io.renren.cms.service.InformationBrowsService;

/**
 * 资讯浏览记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("informationBrowsService")
public class InformationBrowsServiceImpl implements InformationBrowsService {

	@Autowired
	private InformationBrowsDao informationBrowsDao;
	
	@Override
	public InformationBrowsEntity queryObject(Integer id){
		return informationBrowsDao.queryObject(id);
	}
	
	@Override
	public List<InformationBrowsEntity> queryList(Map<String, Object> map){
		return informationBrowsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return informationBrowsDao.queryTotal(map);
	}
	
	@Override
	public void save(InformationBrowsEntity informationBrows){
		informationBrowsDao.save(informationBrows);
	}
	
	@Override
	public void update(InformationBrowsEntity informationBrows){
		informationBrowsDao.update(informationBrows);
	}
	
	@Override
	public void delete(Integer id){
		informationBrowsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		informationBrowsDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return informationBrowsDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return informationBrowsDao.logicDelBatch(ids);
	}

	@Override
	public List<String> queryPortrait(String informationId) {
		return informationBrowsDao.queryPortrait(informationId);
	}


}
