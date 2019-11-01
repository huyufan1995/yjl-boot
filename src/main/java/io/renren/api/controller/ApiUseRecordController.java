package io.renren.api.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.GifTemplateEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.service.GifTemplateService;
import io.renren.cms.service.TemplateService;
import io.renren.cms.service.UseRecordService;
import io.renren.properties.YykjProperties;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("/api/userecord")
@Api("使用记录")
public class ApiUseRecordController {

	@Autowired
	private UseRecordService useRecordService;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private GifTemplateService gifTemplateService;

	@IgnoreAuth
	@ApiOperation(value = "我的使用记录", notes = "我的使用记录")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认10", required = false) })
	@PostMapping("/list")
	public ApiResult list(@ApiIgnore() @RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		List<UseRecordEntity> useRecordList = useRecordService.queryListByGroup(query);
		useRecordList.forEach(u -> {
			if (StringUtils.isNotBlank(u.getTemplateImageExample())) {
				u.setTemplateImageExample(yykjProperties.getVisitprefix().concat(u.getTemplateImageExample()));
			}
			if (StringUtils.equals("static", u.getType())) {
				TemplateEntity templateEntity = templateService.queryObject(u.getTemplateId());
				u.setTemplateUrl(yykjProperties.getVisitprefix().concat(templateEntity.getImageTemplate()));
			} else {
				GifTemplateEntity gifTemplateEntity = gifTemplateService.queryObject(u.getTemplateId());
				u.setTemplateUrl(yykjProperties.getVisitprefix().concat(gifTemplateEntity.getExamplePath()));
			}
		});
		int total = useRecordService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(useRecordList, total, query.getLimit(), query.getPage());
		return ApiResult.ok(pageUtil);
	}

}
