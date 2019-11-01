package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.SessionMember;
import io.renren.api.vo.TagEntityVo;
import io.renren.cms.entity.TagEntity;

/**
 * 标签接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface TagService {

	TagEntity queryObject(Integer id);

	List<TagEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TagEntity tag);

	void update(TagEntity tag);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TagEntity queryObjectByMemberIdAndName(Integer memberId, String name);

	TagEntity queryObjectBySuperiorIdAndName(Integer superiorId, String name);

	List<TagEntity> queryListByClienteleId(Integer clienteleId);

	TagEntity add(String name, Integer clienteleId, SessionMember sessionMember);

	List<TagEntityVo> queryListVo(Map<String, Object> map);

	void deleteFull(Integer tagId);
}
