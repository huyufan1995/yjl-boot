package io.renren.cms.service;

import io.renren.api.dto.CommentEntityDto;
import io.renren.cms.entity.CommentEntity;
import io.renren.cms.vo.CommentEntityVo;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 评论表接口
 * 
 * @author moran
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

	List<String> queryPortrait(String informationId);

	List<CommentEntityDto> queryListDto(Map<String, Object> params);

    List<CommentEntityVo> queryListVO(Map<String, Object> map);
}
