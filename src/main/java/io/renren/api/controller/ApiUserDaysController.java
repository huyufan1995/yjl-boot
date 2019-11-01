package io.renren.api.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.DaysRecordEntity;
import io.renren.cms.entity.UseRuleEntity;
import io.renren.cms.entity.UserDaysEntity;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.DaysRecordService;
import io.renren.cms.service.UseRuleService;
import io.renren.cms.service.UserDaysService;
import io.renren.cms.service.WxUserService;
import io.renren.utils.DateUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
@RestController
@RequestMapping("/api/userdays")
@Api("用户使用权限信息")
public class ApiUserDaysController {
	@Autowired
	private UserDaysService userDaysService;
	@Autowired
	private DaysRecordService daysRecordService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private UseRuleService useRuleService;

	@IgnoreAuth
	@ApiOperation(value = "使用权限信息", notes = "使用权限信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true) })
	@PostMapping("/get_userdays")
	public ApiResult getUserDays(@ApiIgnore() @RequestParam Map<String, Object> params) {
		String openId = (String) params.get("openId");
		Assert.isBlank(openId, "openId为空");
		WxUserEntity user = wxUserService.queryByOpenId(openId);
		Assert.isNull(user, "获取微信会员信息异常");
		Date date = new Date();

		Integer duration = DateUtils.getDistanceDays(user.getCtime(), date).intValue();
		// 查询会员使用天数数据
		Query query = new Query(params);
		UserDaysEntity userDays = userDaysService.queryObjectByParam(query);
		if (null == userDays) {
			UseRuleEntity rule = useRuleService.queryObjectByType("registe");
			userDays = new UserDaysEntity();
			userDays.setCreateTime(date);
			userDays.setIfDeleted("f");
			userDays.setOpenId(openId);
			userDays.setTotalDays(duration + rule.getDays());
			userDays.setRegisteTime(user.getCtime());
			userDays.setUpdateTime(date);

			userDaysService.save(userDays);

			DaysRecordEntity daysRecord = new DaysRecordEntity();
			daysRecord.setCreateTime(date);
			daysRecord.setDays(rule.getDays());
			daysRecord.setIfDeleted("f");
			daysRecord.setOpenId(openId);
			daysRecord.setType("old");
			daysRecord.setUpdateTime(date);
			daysRecordService.save(daysRecord);

			userDays.setRemain_days(rule.getDays());
		} else {
			Integer remainDays = userDays.getTotalDays() - duration;
			userDays.setRemain_days(remainDays > 0 ? remainDays : 0);
		}
		return ApiResult.ok(userDays);
	}

}
