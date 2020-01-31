package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.SessionMember;
import io.renren.cms.entity.MemberEntity;
import io.renren.utils.Query;

/**
 * 会员接口
 * 
 * @author moran
 * @date 2019-11-18 13:51:48
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

	List<MemberEntity> queryListByMemberBanner(Map<String, Object> map);

    List<MemberEntity> queryListByIsCollect(Map<String, Object> map);

	void updateVerify(String verify, String code);

    List<MemberEntity> queryAddressAndNationalityInfo();

	List<MemberEntity> queryListLikeAll(Map<String, Object> params);

    List<MemberEntity> queryListVO(Map<String, Object> params);
}
