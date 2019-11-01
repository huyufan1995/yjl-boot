package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.api.vo.TagEntityVo;
import io.renren.cms.entity.TagEntity;
import io.renren.dao.BaseDao;

/**
 * 标签
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface TagDao extends BaseDao<TagEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TagEntity queryObjectByMemberIdAndName(@Param("memberId") Integer memberId, @Param("name") String name);

	TagEntity queryObjectBySuperiorIdAndName(@Param("superiorId") Integer superiorId, @Param("name") String name);

	List<TagEntity> queryListByClienteleId(Integer clienteleId);

	List<TagEntityVo> queryListVo(Map<String, Object> map);
}
