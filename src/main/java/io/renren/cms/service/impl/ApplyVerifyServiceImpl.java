package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.cms.dao.ApplyVerifyDao;
import io.renren.cms.entity.ApplyVerifyEntity;
import io.renren.cms.service.ApplyVerifyService;

/**
 * 活动核销人员配置服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("applyVerifyService")
public class ApplyVerifyServiceImpl implements ApplyVerifyService {

	@Autowired
	private ApplyVerifyDao applyVerifyDao;
	
	@Override
	public ApplyVerifyEntity queryObject(Integer id){
		return applyVerifyDao.queryObject(id);
	}
	
	@Override
	public List<ApplyVerifyEntity> queryList(Map<String, Object> map){
		return applyVerifyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return applyVerifyDao.queryTotal(map);
	}
	
	@Override
	public void save(ApplyVerifyEntity applyVerify){
		applyVerifyDao.save(applyVerify);
	}
	
	@Override
	public void update(ApplyVerifyEntity applyVerify){
		applyVerifyDao.update(applyVerify);
	}
	
	@Override
	public void delete(Integer id){
		applyVerifyDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		applyVerifyDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return applyVerifyDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return applyVerifyDao.logicDelBatch(ids);
	}

	@Override
	public List<ApplyVerifyEntity> queryListWithVerifyMember(HashMap<String, Object> param) {
		return applyVerifyDao.queryListWithVerifyMember(param);
	}


}
