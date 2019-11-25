package io.renren.cms.service.impl;

import io.renren.api.dto.ApplyRecordEntiyDto;
import io.renren.api.dto.VerifyMemberInfoDto;
import io.renren.cms.vo.ApplyRecordEntityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.cms.dao.ApplyRecordDao;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.service.ApplyRecordService;

/**
 * 活动报名记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("applyRecordService")
public class ApplyRecordServiceImpl implements ApplyRecordService {

	@Autowired
	private ApplyRecordDao applyRecordDao;
	
	@Override
	public ApplyRecordEntity queryObject(Integer id){
		return applyRecordDao.queryObject(id);
	}
	
	@Override
	public List<ApplyRecordEntity> queryList(Map<String, Object> map){
		return applyRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return applyRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(ApplyRecordEntity applyRecord){
		applyRecordDao.save(applyRecord);
	}
	
	@Override
	public void update(ApplyRecordEntity applyRecord){
		applyRecordDao.update(applyRecord);
	}
	
	@Override
	public void delete(Integer id){
		applyRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
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
	public List<ApplyRecordEntiyDto> queryPortrait(HashMap<String, Object> query) {
		return applyRecordDao.queryPortrait(query);
	}

	@Override
	public Boolean deleteByOpenIdAndApplyId(HashMap<String, Object> params) {
		return applyRecordDao.deleteByOpenIdAndApplyId(params);
	}

	@Override
	public List<VerifyMemberInfoDto> queryVerifyMember(String code) {
		return applyRecordDao.queryVerifyMember(code);
	}

	@Override
	public List<ApplyRecordEntityVO> queryListVo(Map<String, Object> map) {
		return applyRecordDao.queryListVo(map);
	}


}
