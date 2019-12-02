package io.renren.cms.service;

import io.renren.api.dto.VerifyApplyDto;
import io.renren.api.dto.VerifyRecordInfoDto;
import io.renren.cms.entity.VerifyRecordEntity;
import io.renren.cms.vo.VerifyRecordEntityVO;
import io.renren.utils.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核销记录接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:37
 */
public interface VerifyRecordService {
	
	VerifyRecordEntity queryObject(Integer id);
	
	List<VerifyRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(VerifyRecordEntity verifyRecord);
	
	void update(VerifyRecordEntity verifyRecord);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<VerifyApplyDto> queryVerifyRecord(Map<String, Object> map);

	List<String> queryPortrait(String id);

	List<VerifyRecordInfoDto> queryVerifyPeopleInfo(Map<String, Object> param);

    boolean updateVerifyStatus(String memberId,String applyId);

    List<VerifyRecordEntityVO> queryListVo(Map<String, Object> param);
}
