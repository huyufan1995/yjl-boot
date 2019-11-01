package io.renren.api.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.component.TemplateMsgHandler;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.LeaveEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.CardService;
import io.renren.cms.service.LeaveService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.SubscibeService;
import io.renren.enums.TemplateMsgTypeEnum;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 留言
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@Api("留言")
@RestController
@RequestMapping("/api/leave")
public class ApiLeaveController {
	
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private CardService cardService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private TemplateMsgHandler templateMsgHandler;
	@Autowired
	private SubscibeService subscibeService;

	@PostMapping("/search_list")
	@ApiOperation(value = "留言管理列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司, dept部门", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult searchList(
						@RequestParam(value = "ownerOpenid", required = false) String ownerOpenid,
						@RequestParam(value = "type", required = true, defaultValue = "my") String type,
						@RequestParam(value = "key", required = false) String key,
						@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
						@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
						@ApiIgnore @TokenMember SessionMember sessionMember
			) {
		
		HashMap<String, Object> params = Maps.newHashMap();
		
		if (StringUtils.equals("my", type)) {
			//普通员工
			if (StringUtils.isNotBlank(ownerOpenid)) {
				MemberEntity memberEntity = memberService.queryObjectByOpenid(ownerOpenid);
				if (memberEntity != null) {
					params.put("memberId", memberEntity.getId());
				}
			} else {
				//员工或者查看自己客户数据
				params.put("memberId", sessionMember.getMemberId());
			}

		} else if (StringUtils.equals("company", type)) {
			//公司
			params.put("superiorId", sessionMember.getSuperiorId());
		} else if (StringUtils.equals("dept", type)) {
			//部门
			params.put("deptId", sessionMember.getDeptId());
		}
		
		params.put("key", key);
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<LeaveEntity> leaveList = leaveService.searchList(query);
		return ApiResult.ok(leaveList);
	}
	
	@PostMapping("/view")
	@ApiOperation(value = "查看留言")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "leaveId", value = "留言ID", required = true)
	})
	public ApiResult view(@RequestParam Integer leaveId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		LeaveEntity leaveEntity = leaveService.queryObject(leaveId);
		Assert.isNullApi(leaveEntity, "该留言不存在");
		
		if(leaveEntity.getMemberId().equals(sessionMember.getMemberId())) {
			//只能标记自己的留言
			if(StringUtils.equals(SystemConstant.F_STR, leaveEntity.getStatus())) {
				//未读
				LeaveEntity temp = new LeaveEntity();
				temp.setId(leaveEntity.getId());
				//已读
				temp.setStatus(SystemConstant.T_STR);
				leaveService.update(temp);
			}
		}
		
		MemberEntity memberEntity = memberService.queryObject(leaveEntity.getMemberId());
		leaveEntity.setRealName(memberEntity.getRealName());
		return ApiResult.ok(leaveEntity);
	}

	@IgnoreAuth
	@PostMapping("/add")
	@ApiOperation(value = "添加留言")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "留言人姓名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "留言人电话", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "留言人openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "content", value = "内容", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "接收人会员ID", required = true)
	})
	public ApiResult add(@ApiIgnore @Validated LeaveEntity leaveEntity) {
		leaveEntity.setCtime(new Date());
		leaveEntity.setIsDel(SystemConstant.F_STR);
		CardEntity cardEntity = cardService.queryObjectByOpenid(leaveEntity.getOpenid());
		if(cardEntity != null && StringUtils.isNotBlank(cardEntity.getPortrait())) {
			leaveEntity.setPortrait(cardEntity.getPortrait());
		} else {
			leaveEntity.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
		}
		leaveEntity.setStatus(SystemConstant.F_STR);
		leaveService.save(leaveEntity);
		
		MemberEntity memberEntity = memberService.queryObject(leaveEntity.getMemberId());
		Assert.isNullApi(memberEntity, "接收人不存在");
		//留言模板消息
		SubscibeEntity subscibeEntity = subscibeService.queryObjectByOpenid(memberEntity.getOpenid());
		if (subscibeEntity != null) {
			templateMsgHandler.sendAsync(SystemConstant.APP_PAGE_PATH_HOME, 
					memberEntity.getOpenid(),
					TemplateMsgTypeEnum.LEAVE.getTemplateId(),
					subscibeEntity.getFormid(),
					DateUtil.formatDateTime(leaveEntity.getCtime()),
					leaveEntity.getName(),
					leaveEntity.getMobile(),
					leaveEntity.getContent());
		}
		return ApiResult.ok();
	}
	
	@ApiOperation("留言删除")
	@PostMapping("/leave_delete")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "leaveId", value = "微官网留言ID", required = true)
	})
	public ApiResult leaveDelete(@RequestParam Integer leaveId) {
		leaveService.logicDel(leaveId);
		return ApiResult.ok();
	}
}
