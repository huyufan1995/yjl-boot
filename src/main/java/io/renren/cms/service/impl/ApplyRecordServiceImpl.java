package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.api.vo.ApplyRecordEntityVo;
import io.renren.api.vo.ApplyRecordVo;
import io.renren.cms.dao.ApplyRecordDao;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.service.ApplyRecordService;

/**
 * 报名记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("applyRecordService")
public class ApplyRecordServiceImpl implements ApplyRecordService {

	@Autowired
	private ApplyRecordDao applyRecordDao;

	@Override
	public ApplyRecordEntity queryObject(Integer id) {
		return applyRecordDao.queryObject(id);
	}

	@Override
	public List<ApplyRecordEntity> queryList(Map<String, Object> map) {
		return applyRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return applyRecordDao.queryTotal(map);
	}

	@Override
	public void save(ApplyRecordEntity applyRecord) {
		applyRecordDao.save(applyRecord);
	}

	@Override
	public void update(ApplyRecordEntity applyRecord) {
		applyRecordDao.update(applyRecord);
	}

	@Override
	public void delete(Integer id) {
		applyRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		applyRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return applyRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return applyRecordDao.logicDelBatch(ids);
	}

	@Override
	public ApplyRecordEntity queryObjectByApplyIdAndJoinOpenid(Integer applyId, String joinOpenid) {
		return applyRecordDao.queryObjectByApplyIdAndJoinOpenid(applyId, joinOpenid);
	}

	@Override
	public List<ApplyRecordVo> queryListByJoinOpenid(Map<String, Object> map) {
		return applyRecordDao.queryListByJoinOpenid(map);
	}

	@Override
	public List<ApplyRecordEntity> queryListByApplyId(Map<String, Object> map) {
		return applyRecordDao.queryListByApplyId(map);
	}

	@Override
	public List<ApplyRecordEntityVo> queryListVo(Map<String, Object> map) {
		return applyRecordDao.queryListVo(map);
	}

	@Override
	public List<ApplyRecordEntity> applyRecordListByApplyId(String id) {
		return applyRecordDao.applyRecordListByApplyId(id);
	}

	@Override
	public int logicDelByApplyId(Integer applyId) {
		return applyRecordDao.logicDelByApplyId(applyId);
	}

}
