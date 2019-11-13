package io.renren.cms.dao;

import java.util.HashMap;
import java.util.List;

import io.renren.api.dto.ApplyRecordEntiyDto;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 活动报名记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 09:58:16
 */
public interface ApplyRecordDao extends BaseDao<ApplyRecordEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<ApplyRecordEntiyDto> queryPortrait(HashMap<String, Object> query);

	Boolean deleteByOpenIdAndApplyId(HashMap<String, Object> params);
}
