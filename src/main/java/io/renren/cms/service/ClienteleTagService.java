package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleTagEntityVo;
import io.renren.cms.entity.ClienteleTagEntity;

/**
 * 客户标签接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface ClienteleTagService {

	ClienteleTagEntity queryObject(Integer id);

	List<ClienteleTagEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(ClienteleTagEntity clienteleTag);

	void update(ClienteleTagEntity clienteleTag);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	int deleteByClienteleIdAndMemberId(Integer clienteleId, Integer memberId);

	int deleteByClienteleIdAndMemberIdAndTagId(Integer clienteleId, Integer memberId, Integer tagId);

	int deleteByClienteleIdAndTagId(Integer clienteleId, Integer tagId);

	List<ClienteleTagEntityVo> queryListVo(Map<String, Object> map);

	int deleteByTagId(Integer tagId);
}
