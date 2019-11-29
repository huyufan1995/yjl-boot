package io.renren.cms.service;

import io.renren.api.dto.LeaveEntityDto;
import io.renren.cms.entity.LeaveEntity;
import io.renren.cms.vo.LeaveEntityVO;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 留言接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 14:32:10
 */
public interface LeaveService {
	
	LeaveEntity queryObject(Integer id);
	
	List<LeaveEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LeaveEntity leave);
	
	void update(LeaveEntity leave);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	List<LeaveEntityDto> queryListByMemberId(Map<String, Object> map);

    void updateMsgStatus(Integer memberId);

	List<LeaveEntityDto> queryListDto(Map<String, Object> map);

    List<LeaveEntityVO> queryListVO(Map<String, Object> map);
}
