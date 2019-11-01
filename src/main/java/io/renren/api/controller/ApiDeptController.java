package io.renren.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import cn.hutool.core.collection.CollectionUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.StaffVo;
import io.renren.cms.entity.DeptEntity;
import io.renren.cms.service.DeptMemberService;
import io.renren.cms.service.DeptService;
import io.renren.cms.service.MemberService;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 部门
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
@Api("部门")
@RestController
@RequestMapping("/api/dept")
public class ApiDeptController {

	@Autowired
	private DeptService deptService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private DeptMemberService deptMemberService;
	
	@ApiOperation(value = "所有部门")
	@PostMapping("/all")
	public ApiResult all(@ApiIgnore @TokenMember SessionMember sessionMember) {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("superiorId", sessionMember.getSuperiorId());
		List<DeptEntity> deptList = deptService.queryList(params);
		if (CollectionUtil.isEmpty(deptList)) {
			//没有部门默认创建3个部门
			DeptEntity deptEntity1 = new DeptEntity();
			deptEntity1.setIsDel(SystemConstant.F_STR);
			deptEntity1.setName("部门1");
			deptEntity1.setSuperiorId(sessionMember.getSuperiorId());
			deptService.save(deptEntity1);

			DeptEntity deptEntity2 = new DeptEntity();
			deptEntity2.setIsDel(SystemConstant.F_STR);
			deptEntity2.setName("部门2");
			deptEntity2.setSuperiorId(sessionMember.getSuperiorId());
			deptService.save(deptEntity2);
			
			DeptEntity deptEntity3 = new DeptEntity();
			deptEntity3.setIsDel(SystemConstant.F_STR);
			deptEntity3.setName("部门3");
			deptEntity3.setSuperiorId(sessionMember.getSuperiorId());
			deptService.save(deptEntity3);
			
			deptList.add(deptEntity3);
			deptList.add(deptEntity2);
			deptList.add(deptEntity1);
		}
		return ApiResult.ok(deptList);
	}
	
	
	@PostMapping("/add")
	@ApiOperation(value = "添加（修改）部门")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "部门ID【添加为空】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "部门名称", required = true)
	})
	public ApiResult add(@RequestParam(value = "id", required = false) Integer id,
			@RequestParam String name,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		if (id == null) {
			HashMap<String, Object> params = Maps.newHashMap();
			params.put("superiorId", sessionMember.getSuperiorId());
			params.put("name", name.trim());
			int total = deptService.queryTotal(params);
			if (total > 0) {
				return ApiResult.error(500, name.trim() + "已存在");
			}

			DeptEntity deptEntity = new DeptEntity();
			deptEntity.setIsDel(SystemConstant.F_STR);
			deptEntity.setName(name.trim());
			deptEntity.setSuperiorId(sessionMember.getSuperiorId());
			deptService.save(deptEntity);
		} else {
			DeptEntity deptEntity = new DeptEntity();
			deptEntity.setId(id);
			deptEntity.setName(name.trim());
			deptService.update(deptEntity);
		}

		return ApiResult.ok();
	}
	
	@PostMapping("/delete")
	@ApiOperation(value = "删除部门")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "部门ID", required = true) })
	public ApiResult delete(@RequestParam Integer id, @ApiIgnore @TokenMember SessionMember sessionMember) {

		DeptEntity deptEntity = deptService.queryObject(id);
		Assert.isNullApi(deptEntity, "该部门不存在");
		if (!sessionMember.getSuperiorId().equals(deptEntity.getSuperiorId())) {
			return ApiResult.error(500, "无权操作");
		}
		deptService.deleteFull(id);
		return ApiResult.ok();
	}
	
	@PostMapping("/staff/list")
	@ApiOperation(value = "部门员工列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "deptId", value = "部门ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false)
	})
	public ApiResult staffList(@ApiIgnore @RequestParam Map<String, Object> params) {
		DeptEntity deptEntity = deptService.queryObject(Integer.valueOf(params.get("deptId").toString()));
		Assert.isNullApi(deptEntity, "该部门不存在");
		List<StaffVo> staffList = memberService.queryListDeptStaff(params);
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("deptName", deptEntity.getName());
		resultMap.put("staffs", staffList);
		return ApiResult.ok(resultMap);
	}
	
	@PostMapping("/staff/off_list")
	@ApiOperation(value = "员工列表【无部门】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false)
	})
	public ApiResult staffOffList(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore @TokenMember SessionMember sessionMember) {
		params.put("superiorId", sessionMember.getSuperiorId());
		List<StaffVo> staffList = memberService.queryListDeptStaffOff(params);
		return ApiResult.ok(staffList);
	}
	
	@PostMapping("/staff/remove")
	@ApiOperation(value = "移除部门")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true)
	})
	public ApiResult staffRemove(@RequestParam Integer memberId) {
		deptMemberService.deleteByMemberId(memberId);
		return ApiResult.ok();
	}
	
	@PostMapping("/staff/leader")
	@ApiOperation(value = "设置主管")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true)
	})
	public ApiResult staffLeader(@RequestParam Integer memberId) {
		deptMemberService.changeLeader(memberId);
		return ApiResult.ok();
	}
	
	@PostMapping("/staff/change_dept")
	@ApiOperation(value = "更改部门")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "memberIds", value = "待加入会员ID,多个逗号相隔", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "deptId", value = "部门ID", required = true)
	})
	public ApiResult changeDept(@RequestParam String memberIds, @RequestParam Integer deptId){
		deptService.changeDept(memberIds.split(","), deptId);
		return ApiResult.ok();
	}
	
	@IgnoreAuth
	@PostMapping("/join")
	@ApiOperation(value = "加入部门")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "加入人openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "deptId", value = "部门ID", required = true)
	})
	public ApiResult join(@RequestParam String openid, @RequestParam Integer deptId){
		deptService.join(openid, deptId);
		return ApiResult.ok();
	}
	

}
