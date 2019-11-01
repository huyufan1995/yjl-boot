package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptEntityVo;
import io.renren.api.vo.DeptMemberEntityVo;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.dao.BaseDao;

/**
 * 部门-会员-关系
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
public interface DeptMemberDao extends BaseDao<DeptMemberEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	int deleteByDeptId(Integer deptId);

	int deleteByMemberId(Integer memberId);

	DeptMemberEntity queryObjectByMemberId(Integer memberId);

	int updateByDeptId(Integer deptId);

    List<DeptMemberEntityVo> queryListVo(Map<String, Object> map);
}
