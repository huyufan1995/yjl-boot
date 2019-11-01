package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.SessionMember;
import io.renren.api.vo.MemberEntityVo;
import io.renren.api.vo.StaffVo;
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

	List<StaffVo> queryListStaff(Map<String, Object> params);

	List<StaffVo> queryListStaffShift(Map<String, Object> params);

	List<StaffVo> queryListDeptStaff(Map<String, Object> params);

	List<StaffVo> queryListDeptStaffOff(Map<String, Object> params);

	int queryTotalStaffCount(Integer superiorId);

	SessionMember login(String openid);

	void addStaff(Integer superiorId, String openid, String role);

	void shift(Integer sourceMemberId, Integer targetMemberId, SessionMember sessionMember);

	void unbind(Integer targetMemberId, Integer operationMemberId);

	int updateBySuperiorId(MemberEntity memberEntity);

	MemberEntity queryObjectByCode(String code);

	SessionMember activate(String openid, String mobile, String acode);

	List<MemberEntityVo> queryListVO(Map<String, Object> map);

	List<MemberEntityVo> queryListBySuperiorId(Map<String, Object> map);

	List<MemberEntityVo> queryListByCompany(Map<String, Object> map);

	int updateMemberId(Map<String, Object> map);

	int updateMemberIdAndOpenid(Map<String, Object> map);

	Boolean saveVipMember(MemberEntity member);

	Boolean forbiddenMember(Integer id);

	Boolean openMember(Integer id);

	MemberEntity queryObjectByMobile(String mobile);

	void enableVip(String mobile);

	void changeRole(MemberEntity temp);
}
