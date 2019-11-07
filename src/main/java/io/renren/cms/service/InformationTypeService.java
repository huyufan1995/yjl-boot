package io.renren.cms.service;

import io.renren.cms.entity.InformationTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-07 17:22:16
 */
public interface InformationTypeService {
	
	InformationTypeEntity queryObject(Integer id);
	
	List<InformationTypeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InformationTypeEntity informationType);
	
	void update(InformationTypeEntity informationType);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
