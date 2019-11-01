package io.renren.api.controller;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.cms.service.ApplyService;
import io.renren.cms.service.CardAccessRecordService;
import io.renren.cms.service.CardHolderService;
import io.renren.cms.service.CaseBrowseService;
import io.renren.cms.service.CaseLeaveService;
import io.renren.cms.service.ClienteleService;
import io.renren.cms.service.LeaveService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.TemplateWebsiteBrowseService;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api("数据统计")
@RestController
@RequestMapping("/api/data")
public class ApiDataController {

	@Autowired
	private LeaveService leaveService;
	@Autowired
	private ClienteleService clienteleService;
	@Autowired
	private CardAccessRecordService cardAccessRecordService;
	@Autowired
	private CardHolderService cardHolderService;
	@Autowired
	private TemplateWebsiteBrowseService templateWebsiteBrowseService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ApplyService applyService;
	@Autowired
	private ApplyRecordService applyRecordService;
	@Autowired
	private CaseLeaveService caseLeaveService;
	@Autowired
	private CaseBrowseService caseBrowseService;

	@ApiOperation(value = "今日数据")
	@PostMapping("/today_data")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司, dept部门", required = false) })
	public ApiResult todayDat(@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		int leaveTotal = 0, leaveAllTotal = 0, clienteleTotal = 0, clienteleTodayTotal = 0, cardAccessRecordTotal = 0, websiteTotal = 0, applyRecordTotal = 0, applyViewTotal = 0,
				caseLeaveAllTotal = 0, caseLeaveTotal = 0, caseBrowseTotal = 0;
		
		if (StringUtils.equals(type, "my")) {
			//普通员工
			HashMap<String, Object> params = Maps.newHashMap();
			
			params.put("shareMemberId", sessionMember.getMemberId());
			caseLeaveAllTotal = caseLeaveService.queryTotal(params);//案例库留言【全部】
			caseBrowseTotal = caseBrowseService.queryTotal(params);
			
			params.put("cardOpenid", sessionMember.getOpenid());
			cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
			
			params.put("ownerOpenid", sessionMember.getOpenid());
			websiteTotal = templateWebsiteBrowseService.queryTotal(params);//官网访客
			
			applyRecordTotal = applyRecordService.queryTotal(params);//报名线索数
			
			params.put("memberId", sessionMember.getMemberId());
			clienteleTotal = clienteleService.queryTotal(params);//累计客户数量
			applyViewTotal = applyService.querySumViewCount(params);//活动报名浏览数
			leaveAllTotal = leaveService.queryTotal(params);//官网留言【全部】
			params.put("status", SystemConstant.F_STR);
			leaveTotal = leaveService.queryTotal(params);//官网留言【未读】
			caseLeaveTotal = caseLeaveService.queryTotal(params);//案例库留【未读】
			
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
			clienteleTodayTotal = clienteleService.queryTotal(params);//今日录入客户数量
		} else if (StringUtils.equals(type, "dept")) {
			//部门主管
			HashMap<String, Object> params = Maps.newHashMap();
			
			params.put("deptId", sessionMember.getDeptId());
			caseLeaveAllTotal = caseLeaveService.queryTotal(params);//案例库留言【全部】
			caseBrowseTotal = caseBrowseService.queryTotal(params);
			
			cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
			
			websiteTotal = templateWebsiteBrowseService.queryTotal(params);//官网访客
			
			applyRecordTotal = applyRecordService.queryTotal(params);//报名线索数】
			
			clienteleTotal = clienteleService.queryTotal(params);//累计客户数量
			applyViewTotal = applyService.querySumViewCount(params);//活动报名浏览数
			leaveAllTotal = leaveService.queryTotal(params);//官网留言【全部】
			params.put("status", SystemConstant.F_STR);
			leaveTotal = leaveService.queryTotal(params);//官网留言【未读】
			caseLeaveTotal = caseLeaveService.queryTotal(params);//案例库留【未读】
			
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
			clienteleTodayTotal = clienteleService.queryTotal(params);//今日录入客户数量
		} else if(StringUtils.equals(type, "company")) {
			//管理员
			HashMap<String, Object> params = Maps.newHashMap();
			params.put("superiorId", sessionMember.getSuperiorId());
			applyViewTotal = applyService.querySumViewCount(params);//活动报名浏览数
			caseBrowseTotal = caseBrowseService.queryTotal(params);
			caseLeaveAllTotal = caseLeaveService.queryTotal(params);//案例库留言【全部】
			leaveAllTotal = leaveService.queryTotal(params);//官网留言【全部】
			params.put("status", SystemConstant.F_STR);
			leaveTotal = leaveService.queryTotal(params);//官网留言【未读】
			caseLeaveTotal = caseLeaveService.queryTotal(params);//案例库留言【未读】
			
			clienteleTotal = clienteleService.queryTotal(params);//累计客户数量
			
			cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
			
			websiteTotal = templateWebsiteBrowseService.queryTotal(params);//官网访客
			
			applyRecordTotal = applyRecordService.queryTotal(params);//报名线索数
			
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
			clienteleTodayTotal = clienteleService.queryTotal(params);//今日录入客户数量
		}
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("leaveTotal", leaveTotal);
		resultJson.put("clienteleTotal", clienteleTotal);
		resultJson.put("clienteleTodayTotal", clienteleTodayTotal);
		resultJson.put("cardAccessRecordTotal", cardAccessRecordTotal);
		resultJson.put("websiteTotal", websiteTotal);
		resultJson.put("applyRecordTotal", applyRecordTotal + leaveAllTotal - leaveTotal);
		resultJson.put("clueTotal", applyRecordTotal + leaveAllTotal + caseLeaveAllTotal);
		resultJson.put("applyViewTotal", applyViewTotal);
		resultJson.put("caseBrowseTotal", caseBrowseTotal);
		
		JSONArray leaves = new JSONArray();
		if(caseLeaveTotal > 0) {
			JSONObject caseLeaveJson = new JSONObject();
			caseLeaveJson.put("type", "case");
			caseLeaveJson.put("val", caseLeaveTotal);
			leaves.add(caseLeaveJson);
		}
		if(leaveTotal > 0) {
			JSONObject websiteLeaveJson = new JSONObject();
			websiteLeaveJson.put("type", "website");
			websiteLeaveJson.put("val", leaveTotal);
			leaves.add(websiteLeaveJson);
		}
		resultJson.put("leaves", leaves);
		return ApiResult.ok(resultJson);
	}
	
	@ApiOperation(value = "数据报表【更多数据】")
	@PostMapping("/data_report")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司，dept部门", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "date", value = "日期 总计all, 今日today, 昨日yesterday, 具体时间2019-08-09", required = false)
	})
	public ApiResult dataReport(
			@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "date", required = true, defaultValue = "all") String date,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		int leaveTotal = 0, clienteleTotal = 0, cardAccessRecordTotal = 0, websiteTotal = 0, cardHolderTotal = 0, applyTotal = 0, applyViewTotal = 0, applyRecordTotal = 0,
				caseLeaveTotal = 0, caseBrowseTotal = 0;
		
		HashMap<String, Object> params = Maps.newHashMap();
		if(StringUtils.equals(date, "today")) {
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
		}else if (StringUtils.equals(date, "yesterday")) {
			params.put("sdate", DateUtil.yesterday().toDateStr());
			params.put("edate", DateUtil.yesterday().toDateStr());
		}else if (StringUtils.equals(date, "all")) {
			
		}else {
			params.put("sdate", date);
			params.put("edate", date);
		}
		
		if (StringUtils.equals(type, "my")) {
			//普通员工
			params.put("memberId", sessionMember.getMemberId());
			leaveTotal = leaveService.queryTotal(params);//留言数量
			clienteleTotal = clienteleService.queryTotal(params);//客户数量
			params.put("cardOpenid", sessionMember.getOpenid());
			cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
			params.put("ownerOpenid", sessionMember.getOpenid());
			websiteTotal = templateWebsiteBrowseService.queryTotal(params);//今日官网访客
			applyTotal = applyService.queryTotal(params);//活动数
			applyViewTotal = applyService.querySumViewCount(params);//活动浏览数
			params.remove("memberId");
			
			params.put("shareMemberId", sessionMember.getMemberId());
			caseLeaveTotal = caseLeaveService.queryTotal(params);//案例库留言数量
			caseBrowseTotal = caseBrowseService.queryTotal(params);//案例库访客量
			applyRecordTotal = applyRecordService.queryTotal(params);//总客户线索数
			
			params.put("openid", sessionMember.getOpenid());
			cardHolderTotal = cardHolderService.queryTotal(params);//名片夹数量
			
		} else if(StringUtils.equals(type, "dept")) {
			//部门主管
			params.put("deptId", sessionMember.getDeptId());
			leaveTotal = leaveService.queryTotal(params);//留言数量
			clienteleTotal = clienteleService.queryTotal(params);//客户数量
			cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
			websiteTotal = templateWebsiteBrowseService.queryTotal(params);//今日官网访客
			applyTotal = applyService.queryTotal(params);//活动数
			applyViewTotal = applyService.querySumViewCount(params);//活动浏览数
			caseLeaveTotal = caseLeaveService.queryTotal(params);//案例库留言数量
			caseBrowseTotal = caseBrowseService.queryTotal(params);//案例库访客量
			applyRecordTotal = applyRecordService.queryTotal(params);//总客户线索数
			cardHolderTotal = cardHolderService.queryTotal(params);//名片夹数量
			
		} else if(StringUtils.equals(type, "company")) {
			//公司
			params.put("superiorId", sessionMember.getSuperiorId());
			leaveTotal = leaveService.queryTotal(params);//未读留言数量
			clienteleTotal = clienteleService.queryTotal(params);//累计客户数量
			cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
			websiteTotal = templateWebsiteBrowseService.queryTotal(params);//官网访客
			cardHolderTotal = cardHolderService.queryTotal(params);//名片夹数量
			applyTotal = applyService.queryTotal(params);//活动数
			applyViewTotal = applyService.querySumViewCount(params);//活动浏览数
			caseLeaveTotal = caseLeaveService.queryTotal(params);//案例库留言数量
			caseBrowseTotal = caseBrowseService.queryTotal(params);//案例库访客量
			applyRecordTotal = applyRecordService.queryTotal(params);//总客户线索数
		}
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("leaveTotal", leaveTotal);
		resultJson.put("clienteleTotal", clienteleTotal);
		resultJson.put("cardAccessRecordTotal", cardAccessRecordTotal);
		resultJson.put("websiteTotal", websiteTotal);
		resultJson.put("cardHolderTotal", cardHolderTotal);
		resultJson.put("applyTotal", applyTotal);
		resultJson.put("applyViewTotal", applyViewTotal);
		resultJson.put("caseLeaveTotal", caseLeaveTotal);
		resultJson.put("caseBrowseTotal", caseBrowseTotal);
		resultJson.put("applyRecordTotal", applyRecordTotal);
		return ApiResult.ok(resultJson);
	}
	
	@ApiOperation(value = "员工数据报表【更多数据】")
	@PostMapping("/member_data_report")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "date", value = "日期 总计all, 今日today, 昨日yesterday, 具体时间2019-08-09", required = false)
	})
	public ApiResult memberDataReport(@RequestParam Integer memberId,
			@RequestParam(value = "date", required = true, defaultValue = "all") String date,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		MemberEntity memberEntity = memberService.queryObject(memberId);
		Assert.isNullApi(memberEntity, "该会员不存在");
		int leaveTotal = 0, clienteleTotal = 0, cardAccessRecordTotal = 0, websiteTotal = 0, cardHolderTotal = 0, applyTotal = 0, applyViewTotal = 0, applyRecordTotal = 0;

		HashMap<String, Object> params = Maps.newHashMap();
		if (StringUtils.equals(date, "today")) {
			params.put("sdate", DateUtil.today());
			params.put("edate", DateUtil.today());
		} else if (StringUtils.equals(date, "yesterday")) {
			params.put("sdate", DateUtil.yesterday().toDateStr());
			params.put("edate", DateUtil.yesterday().toDateStr());
		} else if (StringUtils.equals(date, "all")) {

		} else {
			params.put("sdate", date);
			params.put("edate", date);
		}

		params.put("memberId", memberEntity.getId());
		leaveTotal = leaveService.queryTotal(params);//留言数量
		clienteleTotal = clienteleService.queryTotal(params);//客户数量
		params.put("cardOpenid", memberEntity.getOpenid());
		cardAccessRecordTotal = cardAccessRecordService.queryTotal(params);//名片访客数量
		params.put("ownerOpenid", memberEntity.getOpenid());
		websiteTotal = templateWebsiteBrowseService.queryTotal(params);//今日官网访客
		applyTotal = applyService.queryTotal(params);//活动数
		applyViewTotal = applyService.querySumViewCount(params);//活动浏览数
		
		params.put("shareMemberId", memberEntity.getId());
		applyRecordTotal = applyRecordService.queryTotal(params);//报名线索数
		params.remove("memberId");
		params.put("openid", memberEntity.getOpenid());
		cardHolderTotal = cardHolderService.queryTotal(params);//名片夹数量

		JSONObject resultJson = new JSONObject();
		resultJson.put("leaveTotal", leaveTotal);
		resultJson.put("clienteleTotal", clienteleTotal);
		resultJson.put("cardAccessRecordTotal", cardAccessRecordTotal);
		resultJson.put("websiteTotal", websiteTotal);
		resultJson.put("cardHolderTotal", cardHolderTotal);
		resultJson.put("applyTotal", applyTotal);
		resultJson.put("applyRecordTotal", applyRecordTotal);
		resultJson.put("applyViewTotal", applyViewTotal);
		return ApiResult.ok(resultJson);
	}

}
