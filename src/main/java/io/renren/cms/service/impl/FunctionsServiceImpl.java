package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.FunctionsDao;
import io.renren.cms.entity.FunctionsEntity;
import io.renren.cms.service.FunctionsService;

/**
 * 功能权益服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("functionsService")
public class FunctionsServiceImpl implements FunctionsService {

	@Autowired
	private FunctionsDao functionsDao;
	
	@Override
	public FunctionsEntity queryObject(Integer id){
		return functionsDao.queryObject(id);
	}
	
	@Override
	public List<FunctionsEntity> queryList(Map<String, Object> map){
		return functionsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return functionsDao.queryTotal(map);
	}
	
	@Override
	public void save(FunctionsEntity functions){
		functionsDao.save(functions);
	}
	
	@Override
	public void update(FunctionsEntity functions){
		functionsDao.update(functions);
	}
	
	@Override
	public void delete(Integer id){
		functionsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		functionsDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return functionsDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return functionsDao.logicDelBatch(ids);
	}
	
	
}
