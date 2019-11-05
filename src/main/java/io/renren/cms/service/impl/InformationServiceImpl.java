package io.renren.cms.service.impl;

import io.renren.cms.dao.InformationDao;
import io.renren.cms.entity.InformationsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.service.InformationService;

/**
 * 服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("informationService")
public class InformationServiceImpl implements InformationService {

	@Autowired
	private InformationDao informationDao;
	
	@Override
	public InformationsEntity queryObject(Integer id){
		return informationDao.queryObject(id);
	}
	
	@Override
	public List<InformationsEntity> queryList(Map<String, Object> map){
		return informationDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return informationDao.queryTotal(map);
	}
	
	@Override
	public void save(InformationsEntity information){
		informationDao.save(information);
	}
	
	@Override
	public void update(InformationsEntity information){
		informationDao.update(information);
	}
	
	@Override
	public void delete(Integer id){
		informationDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		informationDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return informationDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return informationDao.logicDelBatch(ids);
	}
	
	
}
