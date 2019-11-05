package io.renren.cms.service;

import io.renren.cms.entity.LikeEntity;

import java.util.List;
import java.util.Map;

/**
 * 点赞表接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
public interface LikeService {
	
	LikeEntity queryObject(Integer id);
	
	List<LikeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LikeEntity like);
	
	void update(LikeEntity like);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
