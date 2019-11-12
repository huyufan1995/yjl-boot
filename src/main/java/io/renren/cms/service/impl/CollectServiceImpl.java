package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.cms.dao.CollectDao;
import io.renren.cms.entity.CollectEntity;
import io.renren.cms.service.CollectService;

/**
 * 收藏服务实现
 * @author huyufan
 *
 */
@Service("collectService")
public class CollectServiceImpl implements CollectService {

	@Autowired
	private CollectDao collectDao;
	
	@Override
	public CollectEntity queryObject(Integer id){
		return collectDao.queryObject(id);
	}
	
	@Override
	public List<CollectEntity> queryList(Map<String, Object> map){
		return collectDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return collectDao.queryTotal(map);
	}
	
	@Override
	public void save(CollectEntity collect){
		collectDao.save(collect);
	}
	
	@Override
	public void update(CollectEntity collect){
		collectDao.update(collect);
	}
	
	@Override
	public void delete(Integer id){
		collectDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		collectDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return collectDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return collectDao.logicDelBatch(ids);
	}

	@Override
	public Boolean isCollect(Integer id, Integer collectType, String openid) {
		return collectDao.isCollect(id,collectType,openid);
	}

	@Override
	public Boolean deleteWithOpenIdAndCollectTypeAndDataId(HashMap<String, Object> params) {
		return collectDao.deleteWithOpenIdAndCollectTypeAndDataId(params);
	}


}
