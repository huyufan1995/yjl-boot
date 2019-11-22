package io.renren.cms.service.impl;

import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.ApplyReviewEntityDto;
import io.renren.cms.entity.ApplyEntity;
import io.renren.enums.AuditStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.ApplyReviewDao;
import io.renren.cms.entity.ApplyReviewEntity;
import io.renren.cms.service.ApplyReviewService;

/**
 * 活动回顾服务实现
 * @author huyufan
 *
 */
@Service("applyReviewService")
public class ApplyReviewServiceImpl implements ApplyReviewService {

	@Autowired
	private ApplyReviewDao applyReviewDao;

	@Override
	public ApplyReviewEntity queryObject(Integer id){
		return applyReviewDao.queryObject(id);
	}

	@Override
	public List<ApplyReviewEntity> queryList(Map<String, Object> map){
		return applyReviewDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		return applyReviewDao.queryTotal(map);
	}

	@Override
	public void save(ApplyReviewEntity applyReview){
		applyReviewDao.save(applyReview);
	}

	@Override
	public void update(ApplyReviewEntity applyReview){
		applyReviewDao.update(applyReview);
	}

	@Override
	public void delete(Integer id){
		applyReviewDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids){
		applyReviewDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return applyReviewDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return applyReviewDao.logicDelBatch(ids);
	}

	@Override
	public int release(Integer id) {
		ApplyReviewEntity entity = new ApplyReviewEntity();
		entity.setId(id);
		entity.setShowStatus(SystemConstant.T_STR);
		entity.setAuditMsg("通过");
		entity.setAuditStatus(AuditStatusEnum.PASS.getCode());
		return applyReviewDao.update(entity);
	}

	@Override
	public ApplyReviewEntityDto queryObjectDto(Map<String, Object> map) {
		return applyReviewDao.queryObjectDto(map);
	}

	@Override
	public int commit(Integer id) {
		ApplyReviewEntity entity = new ApplyReviewEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
		return applyReviewDao.update(entity);
	}

	@Override
	public int revocation(Integer id) {
		ApplyReviewEntity entity = new ApplyReviewEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.UNCOMMIT.getCode());
		return applyReviewDao.update(entity);
	}
}
