package io.renren.cms.service.impl;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.ApplyEntity;
import io.renren.enums.AuditStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.ApplyDao;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.service.ApplyService;

/**
 * 活动服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("applyService")
public class ApplyServiceImpl implements ApplyService {

	@Autowired
	private ApplyDao applyDao;
	
	@Override
	public ApplyEntity queryObject(Integer id){
		return applyDao.queryObject(id);
	}
	
	@Override
	public List<ApplyEntity> queryList(Map<String, Object> map){
		return applyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return applyDao.queryTotal(map);
	}
	
	@Override
	public void save(ApplyEntity apply){
		applyDao.save(apply);
	}
	
	@Override
	public void update(ApplyEntity apply){
		applyDao.update(apply);
	}
	
	@Override
	public void delete(Integer id){
		applyDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		applyDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return applyDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return applyDao.logicDelBatch(ids);
	}

	@Override
	public int release(Integer id) {
		ApplyEntity entity = new ApplyEntity();
		entity.setId(id);
		entity.setShowStatus(SystemConstant.T_STR);
		entity.setAuditMsg("通过");
		entity.setAuditStatus(AuditStatusEnum.PASS.getCode());
		return applyDao.update(entity);
	}

	@Override
	public int commit(Integer id) {
		ApplyEntity entity = new ApplyEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
		return applyDao.update(entity);
	}

	@Override
	public int revocation(Integer id) {
		ApplyEntity entity = new ApplyEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.UNCOMMIT.getCode());
		return applyDao.update(entity);
	}
}
