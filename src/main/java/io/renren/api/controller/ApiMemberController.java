package io.renren.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONObject;
import io.renren.api.component.TemplateMsgHandler;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.CardVo;
import io.renren.api.vo.StaffVo;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.CardAccessRecordService;
import io.renren.cms.service.CardService;
import io.renren.cms.service.ClienteleService;
import io.renren.cms.service.MemberRemovalRecordService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.SubscibeService;
import io.renren.cms.service.TemplateWebsiteBrowseService;
import io.renren.enums.BindStatusEnum;
import io.renren.enums.MemberRoleEnum;
import io.renren.enums.PermTypeEnum;
import io.renren.enums.TemplateMsgTypeEnum;
import io.renren.utils.SystemCache;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 会员
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@Api("会员")
@RestController
@RequestMapping("/api/member")
public class ApiMemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRemovalRecordService memberRemovalRecordService;
	@Autowired
	private ClienteleService clienteleService;
	@Autowired
	private CardAccessRecordService cardAccessRecordService;
	@Autowired
	private TemplateWebsiteBrowseService templateWebsiteBrowseService;
	@Autowired
	private CardService cardService;
	@Autowired
	private SubscibeService subscibeService;
	@Autowired
	private TemplateMsgHandler templateMsgHandler;
	
	@IgnoreAuth
	@PostMapping("/login")
	@ApiOperation(value = "会员登入【调用完微信登入后再调用该接口】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true)
	})
	public ApiResult login(@RequestParam String openid) {
		SessionMember sessionMember = memberService.login(openid);
		return ApiResult.ok(sessionMember);
	}
	
	@IgnoreAuth
	@PostMapping("/add_staff")
	@ApiOperation(value = "添加员工【加入团队】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "superiorId", value = "上级会员ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "当前登入用户openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "role", value = "角色 admin管理员 staff员工", required = true)
	})
	public synchronized ApiResult addStaff(@RequestParam Integer superiorId, @RequestParam String openid, @RequestParam String role) {
		memberService.addStaff(superiorId, openid, role);
		return ApiResult.ok();
	}
	
	@PostMapping("/info")
	@ApiOperation(value = "员工详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true)
	})
	public ApiResult info(@RequestParam Integer memberId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		MemberEntity memberEntity = memberService.queryObject(memberId);
		Assert.isNullApi(memberEntity, "该会员不存在");
		if(!memberEntity.getSuperiorId().equals(sessionMember.getSuperiorId())) {
			return ApiResult.error(500, "无权查看");
		}
		
		//数据
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("memberId", memberEntity.getId());
		int clienteleTotal = clienteleService.queryTotal(params);//累计客户数量
		
		params.put("sdate", DateUtil.today());
		params.put("edate", DateUtil.today());
		int clienteleTodayTotal = clienteleService.queryTotal(params);//今日录入客户数量
		
		params.put("cardOpenid", memberEntity.getOpenid());
		int cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//今日名片访客数量
		
		params.put("ownerOpenid", memberEntity.getOpenid());
		int websiteTotal = templateWebsiteBrowseService.queryTotal(params);//今日官网访客
		JSONObject reportData = new JSONObject();
		reportData.put("clienteleTotal", clienteleTotal);
		reportData.put("clienteleTodayTotal", clienteleTodayTotal);
		reportData.put("cardAccessRecordTotal", cardAccessRecordTotal);
		reportData.put("websiteTotal", websiteTotal);
		
		HashMap<String, Object> resultMap = Maps.newHashMap();
		CardEntity cardEntity = cardService.queryObjectByOpenid(memberEntity.getOpenid());
		CardVo cardVo = new CardVo();
		BeanUtil.copyProperties(cardEntity, cardVo);
		resultMap.put("card", cardVo);
		resultMap.put("reportData", reportData);
		return ApiResult.ok(resultMap);
	}
		
	
	@PostMapping("/delete")
	@ApiOperation(value = "删除员工")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "targetMemberId", value = "目标会员ID", required = true)
	})
	public ApiResult delete(@RequestParam Integer targetMemberId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		
		if(StringUtils.equals(MemberRoleEnum.BOSS.getCode(), sessionMember.getRole())
				|| StringUtils.equals(MemberRoleEnum.ADMIN.getCode(), sessionMember.getRole())) {
			
			MemberEntity targetMember = memberService.queryObject(targetMemberId);
			Assert.isNullApi(targetMember, "目标会员不存在");
			if(!targetMember.getSuperiorId().equals(sessionMember.getSuperiorId())) {
				return ApiResult.error(500, "非法操作");
			}
			if(!StringUtils.equals(BindStatusEnum.UNBIND.getCode(), targetMember.getBindStatus())) {
				return ApiResult.error(500, "只能删除已解绑员工");
			}
			MemberEntity temp = new MemberEntity();
			temp.setId(targetMemberId);
			temp.setIsDel(SystemConstant.T_STR);
			memberService.update(temp);
			return ApiResult.ok();
		} else {
			return ApiResult.error(500, "无权操作");
		}
	}
	
	@PostMapping("/unbind")
	@ApiOperation(value = "解绑")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "targetMemberId", value = "目标会员ID", required = true)
	})
	public ApiResult unbind(@RequestParam Integer targetMemberId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		MemberEntity targetMember = memberService.queryObject(targetMemberId);
		Assert.isNullApi(targetMember, "目标会员不存在");
		if(!targetMember.getSuperiorId().equals(sessionMember.getSuperiorId())) {
			return ApiResult.error(500, "非法操作");
		}
		memberService.unbind(targetMemberId, sessionMember.getMemberId());
		
		//解绑成功通知
		SubscibeEntity subscibeEntity = subscibeService.queryObjectByOpenid(targetMember.getOpenid());
		if (subscibeEntity != null) {
			templateMsgHandler.sendAsync(SystemConstant.APP_PAGE_PATH_INDEX, 
					targetMember.getOpenid(),
					TemplateMsgTypeEnum.UNBIND.getTemplateId(),
					subscibeEntity.getFormid(),
					targetMember.getRealName(),
					targetMember.getCompany(),
					DateUtil.formatDateTime(new Date()),
					"您已被" + sessionMember.getNickname() + "解除团队绑定");
		}
		return ApiResult.ok();
	}
	
	@PostMapping("/bind")
	@ApiOperation(value = "授权绑定")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "targetMemberId", value = "目标会员ID", required = true)
	})
	public ApiResult bind(@RequestParam Integer targetMemberId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		MemberEntity targetMember = memberService.queryObject(targetMemberId);
		Assert.isNullApi(targetMember, "目标会员不存在");
		/*if(!targetMember.getSuperiorId().equals(sessionMember.getSuperiorId())) {
			return ApiResult.error(500, "非法操作");
		}*/
		MemberEntity superiorMember = memberService.queryObject(sessionMember.getSuperiorId());
		Assert.isNullApi(sessionMember, "该团队不存在");
		int staffCount = memberService.queryTotalStaffCount(sessionMember.getSuperiorId()) + 1;//包括自己
		if(superiorMember.getStaffMaxCount() - staffCount <= 0) {
			return ApiResult.error(500, "员工数量已达上限");
		}
		MemberEntity temp = new MemberEntity();
		temp.setId(targetMemberId);
		temp.setAuthStatus(SystemConstant.T_STR);//已经授权
		temp.setBindStatus(BindStatusEnum.BIND.getCode());
		memberService.update(temp);
		return ApiResult.ok();
	}
	
	@PostMapping("/shift")
	@ApiOperation(value = "迁移用户【确认迁移用户】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "sourceMemberId", value = "源会员ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "targetMemberId", value = "目标会员ID", required = true)
	})
	public ApiResult shift(@RequestParam Integer sourceMemberId, @RequestParam Integer targetMemberId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		memberService.shift(sourceMemberId, targetMemberId, sessionMember);
		return ApiResult.ok();
	}
	
	@PostMapping("/staff_shift_list")
	@ApiOperation(value = "迁移用户列表【选择迁移用户】")
	public ApiResult staffShiftList(@ApiIgnore @RequestParam Map<String, Object> params,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		if (StringUtils.equals(PermTypeEnum.LEADER.getCode(), sessionMember.getPerm())) {
			params.put("deptId", sessionMember.getDeptId());
		} else {
			params.put("superiorId", sessionMember.getSuperiorId());
		}
		List<StaffVo> staffList = memberService.queryListStaffShift(params);
		ArrayList<StaffVo> boss = Lists.newArrayList();
		ArrayList<StaffVo> admin = Lists.newArrayList();
		ArrayList<StaffVo> staff = Lists.newArrayList();
		for (StaffVo staffVo : staffList) {
			if(MemberRoleEnum.BOSS.getCode().equals(staffVo.getRole())) {
				boss.add(staffVo);
			}else if (MemberRoleEnum.ADMIN.getCode().equals(staffVo.getRole())) {
				admin.add(staffVo);
			}else if (MemberRoleEnum.STAFF.getCode().equals(staffVo.getRole())) {
				staff.add(staffVo);
			}
		}
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("boss", boss);
		resultMap.put("admin", admin);
		resultMap.put("staff", staff);
		return ApiResult.ok(resultMap);
	}

	@PostMapping("/staff_list")
	@ApiOperation(value = "我的员工【已授权绑定】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false)
	})
	public ApiResult staffList(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore @TokenMember SessionMember sessionMember) {
		params.put("authStatus", SystemConstant.T_STR);
		params.put("bindStatus", BindStatusEnum.BIND.getCode());
		
		if (StringUtils.equals(PermTypeEnum.LEADER.getCode(), sessionMember.getPerm())) {
			params.put("deptId", sessionMember.getDeptId());
		} else {
			params.put("superiorId", sessionMember.getSuperiorId());
		}
		List<StaffVo> staffList = memberService.queryListStaff(params);
		staffList.forEach(item -> {
			//迁移记录
			item.setRemovalRecord(memberRemovalRecordService.queryListBySourceMember(item.getId()));
		});
		return ApiResult.ok(staffList);
	}
	
	@PostMapping("/staff_all")
	@ApiOperation(value = "所有员工【包括未授权，已解绑】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false)
	})
	public ApiResult staffAll(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore @TokenMember SessionMember sessionMember) {
		params.put("superiorId", sessionMember.getSuperiorId());
		List<StaffVo> staffList = memberService.queryListStaff(params);
		return ApiResult.ok(staffList);
	}
	
	@IgnoreAuth
	@PostMapping("/activate")
	@ApiOperation(value = "激活【领取VIP会员】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "vcode", value = "验证码", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "acode", value = "激活码", required = true)
	})
	public ApiResult activate(@RequestParam String openid, @RequestParam String mobile, 
			@RequestParam String vcode, @RequestParam String acode) {
		
		if (!Validator.isMobile(mobile)) {
			return ApiResult.error(500, "手机号格式错误");
		}
		String codeCache = SystemCache.smsCodeCache.get(mobile);
		if (!StringUtils.equals(vcode, codeCache)) {
			return ApiResult.error(500, "验证码错误或失效");
		}
		SessionMember sessionMember = memberService.activate(openid, mobile, acode);
		return ApiResult.ok(sessionMember);
	}
	
	@PostMapping("/change_role")
	@ApiOperation(value = "权限管理【改变角色】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true)
	})
	public ApiResult changeRole(@RequestParam Integer memberId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		if(MemberRoleEnum.ADMIN.getCode().equals(sessionMember.getRole()) 
				|| MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {
			
			if(memberId.equals(sessionMember.getMemberId())) {
				return ApiResult.error(500, "无法更换自己");
			}
			MemberEntity memberEntity = memberService.queryObject(memberId);
			Assert.isNullApi(memberEntity, "该会员不存在");
			if(!sessionMember.getSuperiorId().equals(memberEntity.getSuperiorId())) {
				return ApiResult.error(500, "无权操作");
			}
			if(MemberRoleEnum.BOSS.getCode().equals(memberEntity.getRole())) {
				return ApiResult.error(500, "无权操作");
			}
			String role = MemberRoleEnum.ADMIN.getCode().equals(memberEntity.getRole()) ? MemberRoleEnum.STAFF.getCode()
					: MemberRoleEnum.ADMIN.getCode();
			MemberEntity temp = new MemberEntity();
			temp.setId(memberEntity.getId());
			temp.setRole(role);
			memberService.changeRole(temp);
		} else {
			return ApiResult.error(500, "无权操作");
		}
		return ApiResult.ok();
	}
}
