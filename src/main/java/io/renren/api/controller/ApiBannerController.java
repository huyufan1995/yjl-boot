package io.renren.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.BannerEntity;
import io.renren.cms.service.BannerService;
import io.renren.properties.YykjProperties;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 广告
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-24 12:13:18
 */
@RestController
@RequestMapping("/api/banner")
@Api("banner")
public class ApiBannerController {

	@Autowired
	private BannerService bannerService;
	@Autowired
	private YykjProperties yykjProperties;

	@IgnoreAuth
	@ApiOperation(value = "banner图列表")
	@PostMapping("/location_list")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "location", value = "位置 index crm", required = true) })
	public ApiResult locationList(@ApiIgnore @RequestParam Map<String, Object> params) {
		Assert.isNullApi(params.get("location"), "位置不能为空");
		List<BannerEntity> bannerList = bannerService.queryList(params);
		bannerList.forEach(item -> {
			if (StringUtils.isNotBlank(item.getImagePath()) && !item.getImagePath().startsWith("http")) {
				item.setImagePath(yykjProperties.getVisitprefix().concat(item.getImagePath()));
			}
		});
		return ApiResult.ok(bannerList);
	}

	@Deprecated
	@ApiIgnore
	@IgnoreAuth
	@ApiOperation(value = "广告图")
	@PostMapping("/list")
	public ApiResult list() {
		Map<String, Object> params = new HashMap<>();
		params.put("location", SystemConstant.BANNER_ADVS);
		List<BannerEntity> bannerList = bannerService.queryList(params); // sort_num desc
		bannerList.forEach(b -> {
			if (StringUtils.isNotBlank(b.getImagePath()) && !b.getImagePath().startsWith("http")) {
				b.setImagePath(yykjProperties.getVisitprefix().concat(b.getImagePath()));
			}
		});
		return ApiResult.ok(bannerList);
	}

	@Deprecated
	@ApiIgnore
	@IgnoreAuth
	@ApiOperation(value = "个人中心广告位")
	@PostMapping("/advs")
	public ApiResult advs() {
		BannerEntity b = bannerService.queryObjectByType(SystemConstant.BANNER_ADVS);
		if (StringUtils.isNotBlank(b.getImagePath()) && !b.getImagePath().startsWith("http")) {
			b.setImagePath(yykjProperties.getVisitprefix().concat(b.getImagePath()));
		}
		return ApiResult.ok(b);
	}

}
