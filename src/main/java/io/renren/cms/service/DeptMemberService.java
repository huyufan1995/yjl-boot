package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptMemberEntityVo;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.utils.Query;

/**
 * 部门-会员-关系接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
public interface DeptMemberService {

	DeptMemberEntity queryObject(Integer id);

	List<DeptMemberEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(DeptMemberEntity deptMember);

	void update(DeptMemberEntity deptMember);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	int deleteByDeptId(Integer deptId);

	int deleteByMemberId(Integer memberId);

	DeptMemberEntity queryObjectByMemberId(Integer memberId);

	void changeLeader(Integer memberId);

	int updateByDeptId(Integer deptId);

    List<DeptMemberEntityVo> queryListVo(Map<String, Object> map);
}
