package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.api.vo.ClienteleVo;
import io.renren.cms.entity.ClienteleEntity;
import io.renren.dao.BaseDao;

/**
 * 客户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface ClienteleDao extends BaseDao<ClienteleEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	ClienteleEntity queryObjectByMobile(String mobile);

	ClienteleEntity queryObjectByMobileAndMemberId(@Param("mobile") String mobile, @Param("memberId") Integer memberId);

	List<ClienteleVo> querySearchList(Map<String, Object> map);

	int updateClearMemberId(Integer memberId);

	ClienteleEntityVo queryObjectVo(Integer id);

	List<ClienteleEntityVo> queryListVo(Map<String, Object> map);
}
