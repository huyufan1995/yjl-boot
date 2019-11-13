package io.renren.cms.service;

import io.renren.api.dto.ApplyRecordEntiyDto;
import io.renren.cms.entity.ApplyRecordEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动报名记录接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 09:58:16
 */
public interface ApplyRecordService {
	
	ApplyRecordEntity queryObject(Integer id);
	
	List<ApplyRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ApplyRecordEntity applyRecord);
	
	void update(ApplyRecordEntity applyRecord);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);


	List<ApplyRecordEntiyDto> queryPortrait(HashMap<String, Object> query);

	Boolean deleteByOpenIdAndApplyId(HashMap<String, Object> params);
}
