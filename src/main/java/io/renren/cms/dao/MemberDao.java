package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.MemberEntityVo;
import io.renren.api.vo.StaffVo;
import io.renren.cms.entity.MemberEntity;
import io.renren.dao.BaseDao;

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

	List<StaffVo> queryListStaff(Map<String, Object> params);

	List<StaffVo> queryListStaffShift(Map<String, Object> params);

	List<StaffVo> queryListDeptStaff(Map<String, Object> params);

	List<StaffVo> queryListDeptStaffOff(Map<String, Object> params);

	int queryTotalStaffCount(Integer superiorId);

	int updateBySuperiorId(MemberEntity memberEntity);

	List<MemberEntityVo> queryListVO(Map<String, Object> map);

	List<MemberEntityVo> queryListBySuperiorId(Map<String, Object> map);

	List<MemberEntityVo> queryListByCompany(Map<String, Object> map);

	int updateMemberId(Map<String, Object> map);

	int updateMemberIdAndOpenid(Map<String, Object> map);

	int saveVipMember(MemberEntity member);

	int forbiddenMember(Integer id);

	int openMember(Integer id);

	MemberEntity queryObjectByMobile(String mobile);
}
