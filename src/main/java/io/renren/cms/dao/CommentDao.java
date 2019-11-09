package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.CommentEntityDto;
import io.renren.cms.entity.CommentEntity;
import io.renren.dao.BaseDao;

/**
 * 评论表
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
public interface CommentDao extends BaseDao<CommentEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<String> queryPortrait(String informationId);

	List<CommentEntityDto> queryListDto(Map<String, Object> params);
}
