package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.ApplyEntityDto;
import io.renren.cms.entity.ApplyEntity;
import io.renren.dao.BaseDao;

/**
 * 活动
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 19:02:39
 */
public interface ApplyDao extends BaseDao<ApplyEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<ApplyEntityDto> queryListDto(Map<String, Object> params);

    ApplyEntityDto findAllById(String applyId);
}
