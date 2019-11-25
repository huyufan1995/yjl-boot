package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.MemberEntity;
import io.renren.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * 会员
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface MemberDao extends BaseDao<MemberEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	MemberEntity queryObjectByOpenid(String openid);

	MemberEntity queryObjectByCode(String code);



	int queryTotalStaffCount(Integer superiorId);

	int updateBySuperiorId(MemberEntity memberEntity);


	int updateMemberId(Map<String, Object> map);

	int updateMemberIdAndOpenid(Map<String, Object> map);

	int saveVipMember(MemberEntity member);

	int forbiddenMember(Integer id);

	int openMember(Integer id);

	MemberEntity queryObjectByMobile(String mobile);

	List<MemberEntity> queryListByMemberBanner(Map<String, Object> map);

    List<MemberEntity> queryListByIsCollect(Map<String, Object> map);

    void updateVerify(@Param(value = "verify") String verify,@Param(value = "openid") String openid);

    List<MemberEntity> queryAddressAndNationalityInfo();

	List<MemberEntity> queryListLikeAll(Map<String, Object> params);
}
