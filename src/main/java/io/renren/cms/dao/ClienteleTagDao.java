package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.api.vo.ClienteleTagEntityVo;
import io.renren.cms.entity.ClienteleTagEntity;
import io.renren.dao.BaseDao;

/**
 * 客户标签
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface ClienteleTagDao extends BaseDao<ClienteleTagEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	int deleteByClienteleIdAndMemberId(@Param("clienteleId") Integer clienteleId, @Param("memberId") Integer memberId);

	int deleteByClienteleIdAndMemberIdAndTagId(@Param("clienteleId") Integer clienteleId,
			@Param("memberId") Integer memberId, @Param("tagId") Integer tagId);

	int deleteByClienteleIdAndTagId(@Param("clienteleId") Integer clienteleId, @Param("tagId") Integer tagId);

	List<ClienteleTagEntityVo> queryListVo(Map<String, Object> map);

	int deleteByTagId(Integer tagId);
}
