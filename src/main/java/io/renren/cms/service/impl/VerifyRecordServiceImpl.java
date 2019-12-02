package io.renren.cms.service.impl;

import io.renren.api.dto.VerifyApplyDto;
import io.renren.api.dto.VerifyRecordInfoDto;
import io.renren.cms.vo.VerifyRecordEntityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.VerifyRecordDao;
import io.renren.cms.entity.VerifyRecordEntity;
import io.renren.cms.service.VerifyRecordService;

/**
 * 核销记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("verifyRecordService")
public class VerifyRecordServiceImpl implements VerifyRecordService {

	@Autowired
	private VerifyRecordDao verifyRecordDao;
	
	@Override
	public VerifyRecordEntity queryObject(Integer id){
		return verifyRecordDao.queryObject(id);
	}
	
	@Override
	public List<VerifyRecordEntity> queryList(Map<String, Object> map){
		return verifyRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return verifyRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(VerifyRecordEntity verifyRecord){
		verifyRecordDao.save(verifyRecord);
	}
	
	@Override
	public void update(VerifyRecordEntity verifyRecord){
		verifyRecordDao.update(verifyRecord);
	}
	
	@Override
	public void delete(Integer id){
		verifyRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		verifyRecordDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return verifyRecordDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return verifyRecordDao.logicDelBatch(ids);
	}

	@Override
	public List<VerifyApplyDto> queryVerifyRecord(Map<String, Object> map) {
		return verifyRecordDao.queryVerifyRecord(map);
	}

	@Override
	public List<String> queryPortrait(String id) {
		return verifyRecordDao.queryPortrait(id);
	}

	@Override
	public List<VerifyRecordInfoDto> queryVerifyPeopleInfo(Map<String, Object> param) {
		return verifyRecordDao.queryVerifyPeopleInfo(param);
	}

	@Override
	public boolean updateVerifyStatus(String memberId,String applyId) {
		return verifyRecordDao.updateVerifyStatus(memberId,applyId);
	}

	@Override
	public List<VerifyRecordEntityVO> queryListVo(Map<String, Object> param) {
		return verifyRecordDao.queryListVo(param);
	}


}
