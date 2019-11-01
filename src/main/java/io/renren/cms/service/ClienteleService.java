package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleEntityVo;
import io.renren.api.vo.ClienteleVo;
import io.renren.cms.entity.ClienteleEntity;

/**
 * 客户接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface ClienteleService {

	ClienteleEntity queryObject(Integer id);

	List<ClienteleEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(ClienteleEntity clientele);

	void update(ClienteleEntity clientele);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	void add(ClienteleEntity clienteleEntity, String tags, String remark);

	ClienteleEntity queryObjectByMobile(String mobile);

	List<ClienteleVo> querySearchList(Map<String, Object> map);

	int updateClearMemberId(Integer memberId);

	void modify(ClienteleEntity clienteleEntity, String tags);

	void changeMember(Integer clienteleId, Integer targetMemberId, Integer operationMemberId);

	ClienteleEntity queryObjectByMobileAndMemberId(String mobile, Integer memberId);

	List<ClienteleEntityVo> queryListVo(Map<String, Object> map);

	ClienteleEntityVo queryObjectVo(Integer id);
}
