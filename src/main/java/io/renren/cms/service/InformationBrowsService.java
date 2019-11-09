package io.renren.cms.service;

import io.renren.cms.entity.InformationBrowsEntity;

import java.util.List;
import java.util.Map;

/**
 * 资讯浏览记录接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 14:39:16
 */
public interface InformationBrowsService {
	
	InformationBrowsEntity queryObject(Integer id);
	
	List<InformationBrowsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InformationBrowsEntity informationBrows);
	
	void update(InformationBrowsEntity informationBrows);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	List<String> queryPortrait(String toString);
}
