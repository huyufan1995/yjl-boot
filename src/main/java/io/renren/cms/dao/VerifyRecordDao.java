package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.VerifyApplyDto;
import io.renren.api.dto.VerifyRecordInfoDto;
import io.renren.cms.entity.VerifyRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 核销记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:37
 */
public interface VerifyRecordDao extends BaseDao<VerifyRecordEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	List<VerifyApplyDto> queryVerifyRecord(Map<String, Object> map);

	List<String> queryPortrait(String applyId);

	List<VerifyRecordInfoDto> queryVerifyPeopleInfo(Map<String, Object> param);
}
