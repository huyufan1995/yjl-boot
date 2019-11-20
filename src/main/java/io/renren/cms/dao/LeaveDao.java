package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.LeaveEntityDto;
import io.renren.cms.entity.LeaveEntity;
import io.renren.dao.BaseDao;

/**
 * 留言
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 14:32:10
 */
public interface LeaveDao extends BaseDao<LeaveEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	List<LeaveEntityDto> queryListByMemberId(Map<String, Object> map);

    void updateMsgStatus(Integer memberId);

	List<LeaveEntityDto> queryListDto(Map<String, Object> map);
}
