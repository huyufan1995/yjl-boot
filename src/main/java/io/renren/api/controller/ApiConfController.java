package io.renren.api.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;

import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.FunctionsEntity;
import io.renren.cms.service.FunctionsService;
import io.renren.properties.YykjProperties;
import io.renren.service.SysConfigService;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/conf")
@Api("系统配置")
public class ApiConfController {

	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private FunctionsService functionsService;
	@Autowired
	private YykjProperties yykjProperties;

	@Deprecated
	@IgnoreAuth
	@ApiOperation(value = "功能权益")
	@PostMapping("/functions")
	public ApiResult functions() {
		List<FunctionsEntity> functionsList = functionsService.queryList(null);
		functionsList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getImage()) && !item.getImage().startsWith("http")) {
				item.setImage(yykjProperties.getVisitprefix().concat(item.getImage()));
			}
		});
		return ApiResult.ok(functionsList);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "功能权益V2")
	@PostMapping("/v2/functions")
	public ApiResult functionsV2() {
		List<FunctionsEntity> functionsList = functionsService.queryList(null);
		functionsList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getImage()) && !item.getImage().startsWith("http")) {
				item.setImage(yykjProperties.getVisitprefix().concat(item.getImage()));
			}
		});
		String vipExplain = sysConfigService.getValue(SystemConstant.KEY_VIP_EXPLAIN, "智能营销系统(海报裂变+智能名片+微官网+活动+...)+团队协同+智慧CRM+客户云");
		HashMap<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("functionsList", functionsList);
		resultMap.put("vipExplain", vipExplain);
		return ApiResult.ok(resultMap);
	}

	@IgnoreAuth
	@ApiOperation(value = "VIP单价")
	@PostMapping("/vip_unit_price")
	public ApiResult vipUnitPrice() {
		String vipUnitPrice = sysConfigService.getValue(SystemConstant.KEY_VIP_UNIT_PRICE, "1");
		return ApiResult.ok(vipUnitPrice);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "获取报名默认表单项")
	@PostMapping("/default_apply_item")
	public ApiResult defaultApplyItem() {
		String applyItemStr = sysConfigService.getValue(SystemConstant.KEY_DEFAULT_APPLY_ITEM, "[]");
		JSONArray applyItemArray = JSON.parseArray(applyItemStr);
		return ApiResult.ok(applyItemArray);
	}

}
