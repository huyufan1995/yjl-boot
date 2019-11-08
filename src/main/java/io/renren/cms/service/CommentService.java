package io.renren.cms.service;

import io.renren.cms.entity.CommentEntity;

import java.util.List;
import java.util.Map;

/**
 * 评论表接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
public interface CommentService {
	
	CommentEntity queryObject(Integer id);
	
	List<CommentEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CommentEntity comment);
	
	void update(CommentEntity comment);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	List<String> queryPortrait(int i);
}
