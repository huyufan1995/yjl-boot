package io.renren.cms.service;

import io.renren.api.dto.ApplyEntityDto;
import io.renren.cms.entity.ApplyEntity;

import java.util.List;
import java.util.Map;

/**
 * 活动接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 19:02:39
 */
public interface ApplyService {
	
	ApplyEntity queryObject(Integer id);
	
	List<ApplyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ApplyEntity apply);
	
	void update(ApplyEntity apply);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    int revocation(Integer id);

	int commit(Integer id);

	int release(Integer id);

    List<ApplyEntityDto> queryListDto(Map<String, Object> params);

	ApplyEntityDto findAllById(Integer id);
}
