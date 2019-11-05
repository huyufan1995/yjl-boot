package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.SessionMember;
import io.renren.cms.entity.MemberEntity;

/**
 * 会员接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface MemberService {

	MemberEntity queryObject(Integer id);

	List<MemberEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(MemberEntity member);

	void update(MemberEntity member);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	MemberEntity queryObjectByOpenid(String openid);

	SessionMember login(String openid);

	MemberEntity queryObjectByCode(String code);

}
