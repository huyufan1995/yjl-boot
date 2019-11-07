package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.InformationTypeDao;
import io.renren.cms.entity.InformationTypeEntity;
import io.renren.cms.service.InformationTypeService;

/**
 * 服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("informationTypeService")
public class InformationTypeServiceImpl implements InformationTypeService {

	@Autowired
	private InformationTypeDao informationTypeDao;
	
	@Override
	public InformationTypeEntity queryObject(Integer id){
		return informationTypeDao.queryObject(id);
	}
	
	@Override
	public List<InformationTypeEntity> queryList(Map<String, Object> map){
		return informationTypeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return informationTypeDao.queryTotal(map);
	}
	
	@Override
	public void save(InformationTypeEntity informationType){
		informationTypeDao.save(informationType);
	}
	
	@Override
	public void update(InformationTypeEntity informationType){
		informationTypeDao.update(informationType);
	}
	
	@Override
	public void delete(Integer id){
		informationTypeDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		informationTypeDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return informationTypeDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return informationTypeDao.logicDelBatch(ids);
	}
	
	
}
