package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptMemberEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.cms.dao.DeptMemberDao;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.cms.service.DeptMemberService;
import io.renren.utils.validator.Assert;

/**
 * 部门-会员-关系服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("deptMemberService")
public class DeptMemberServiceImpl implements DeptMemberService {

	@Autowired
	private DeptMemberDao deptMemberDao;

	@Override
	public DeptMemberEntity queryObject(Integer id) {
		return deptMemberDao.queryObject(id);
	}

	@Override
	public List<DeptMemberEntity> queryList(Map<String, Object> map) {
		return deptMemberDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return deptMemberDao.queryTotal(map);
	}

	@Override
	public void save(DeptMemberEntity deptMember) {
		deptMemberDao.save(deptMember);
	}

	@Override
	public void update(DeptMemberEntity deptMember) {
		deptMemberDao.update(deptMember);
	}

	@Override
	public void delete(Integer id) {
		deptMemberDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		deptMemberDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return deptMemberDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return deptMemberDao.logicDelBatch(ids);
	}

	@Override
	public int deleteByDeptId(Integer deptId) {
		return deptMemberDao.deleteByDeptId(deptId);
	}

	@Override
	public int deleteByMemberId(Integer memberId) {
		return deptMemberDao.deleteByMemberId(memberId);
	}

	@Override
	public DeptMemberEntity queryObjectByMemberId(Integer memberId) {
		return deptMemberDao.queryObjectByMemberId(memberId);
	}

	@Override
	@Transactional
	public void changeLeader(Integer memberId) {
		DeptMemberEntity deptMemberEntity = deptMemberDao.queryObjectByMemberId(memberId);
		Assert.isNullApi(deptMemberEntity, "会员不存在");
		deptMemberDao.updateByDeptId(deptMemberEntity.getDeptId());
		deptMemberEntity.setPerm("leader");
		deptMemberDao.update(deptMemberEntity);
	}

	@Override
	public int updateByDeptId(Integer deptId) {
		return deptMemberDao.updateByDeptId(deptId);
	}

	@Override
	public List<DeptMemberEntityVo> queryListVo(Map<String, Object> map) {
		return deptMemberDao.queryListVo(map);
	}

}
