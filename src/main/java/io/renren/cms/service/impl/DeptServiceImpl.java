package io.renren.cms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptEntityVo;
import io.renren.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.api.constant.SystemConstant;
import io.renren.api.exception.ApiException;
import io.renren.cms.dao.DeptDao;
import io.renren.cms.dao.DeptMemberDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.entity.DeptEntity;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.DeptService;
import io.renren.enums.BindStatusEnum;
import io.renren.enums.MemberRoleEnum;
import io.renren.enums.PermTypeEnum;
import io.renren.utils.validator.Assert;

/**
 * 部门服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("deptService")
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptDao deptDao;
	@Autowired
	private DeptMemberDao deptMemberDao;
	@Autowired
	private MemberDao memberDao;

	@Override
	public DeptEntity queryObject(Integer id) {
		return deptDao.queryObject(id);
	}

	@Override
	public List<DeptEntity> queryList(Map<String, Object> map) {
		return deptDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return deptDao.queryTotal(map);
	}

	@Override
	public void save(DeptEntity dept) {
		deptDao.save(dept);
	}

	@Override
	public void update(DeptEntity dept) {
		deptDao.update(dept);
	}

	@Override
	public void delete(Integer id) {
		deptDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		deptDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return deptDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return deptDao.logicDelBatch(ids);
	}

	@Override
	@Transactional
	public void deleteFull(Integer id) {
		deptDao.logicDel(id);
		deptMemberDao.deleteByDeptId(id);
	}

	@Override
	@Transactional
	public void changeDept(String[] memberIds, Integer deptId) {
		Date now = new Date();
		for (String memberId : memberIds) {
			MemberEntity memberEntity = memberDao.queryObject(memberId);
			Assert.isNullApi(memberEntity, "该会员不存在");
			DeptMemberEntity deptMemberEntity = deptMemberDao.queryObjectByMemberId(memberEntity.getId());
			if (deptMemberEntity == null) {
				deptMemberEntity = new DeptMemberEntity();
				deptMemberEntity.setDeptId(deptId);
				deptMemberEntity.setMemberId(memberEntity.getId());
				deptMemberEntity.setOpenid(memberEntity.getOpenid());
				deptMemberEntity.setPerm(PermTypeEnum.EMPLOY.getCode());
				deptMemberEntity.setJoinTime(now);
				deptMemberDao.save(deptMemberEntity);
			} else {
				if(deptId.equals(deptMemberEntity.getDeptId())) {
					throw new ApiException("该用户已在该部门，无法更换！");
				}
				deptMemberEntity.setDeptId(deptId);
				deptMemberEntity.setPerm(PermTypeEnum.EMPLOY.getCode());
				deptMemberEntity.setJoinTime(now);
				deptMemberDao.update(deptMemberEntity);
			}
		}
	}

	@Override
	public void join(String openid, Integer deptId) {
		MemberEntity memberEntity = memberDao.queryObjectByOpenid(openid);
		if(memberEntity == null 
				|| !StringUtils.equals(BindStatusEnum.BIND.getCode(), memberEntity.getBindStatus())
				|| !StringUtils.equals(SystemConstant.T_STR, memberEntity.getAuthStatus())
				|| StringUtils.equals(SystemConstant.T_STR, memberEntity.getIsDel())
				|| StringUtils.equals("freeze", memberEntity.getStatus())) {
			throw new ApiException("您不是VIP，无法加入！");
		}
		
		DeptEntity deptEntity = deptDao.queryObject(deptId);
		Assert.isNullApi(deptEntity, "该部门不存在，无法加入");
		
		if(!memberEntity.getSuperiorId().equals(deptEntity.getSuperiorId())) {
			throw new ApiException("加入失败，请先成为该团队成员");
		}
		
		if(!StringUtils.equals(MemberRoleEnum.STAFF.getCode(), memberEntity.getRole())) {
			throw new ApiException("管理员无法加入部门");
		}
		
		DeptMemberEntity deptMemberEntity = deptMemberDao.queryObjectByMemberId(memberEntity.getId());
		if (deptMemberEntity == null) {
			deptMemberEntity = new DeptMemberEntity();
			deptMemberEntity.setDeptId(deptId);
			deptMemberEntity.setJoinTime(new Date());
			deptMemberEntity.setMemberId(memberEntity.getId());
			deptMemberEntity.setOpenid(memberEntity.getOpenid());
			deptMemberEntity.setPerm(PermTypeEnum.EMPLOY.getCode());
			deptMemberDao.save(deptMemberEntity);
		} else {
			if(deptId.equals(deptMemberEntity.getDeptId())) {
				throw new ApiException("您已经是该部门用户，不能重复加入！");
			}
			deptMemberEntity.setDeptId(deptId);
			deptMemberEntity.setJoinTime(new Date());
			deptMemberEntity.setPerm(PermTypeEnum.EMPLOY.getCode());
			deptMemberDao.update(deptMemberEntity);
		}
	}

	@Override
	public List<DeptEntityVo> queryListVo(Map<String, Object> map) {
		return deptDao.queryListVo(map);
	}

}
