package io.renren.api.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.binarywang.wx.miniapp.api.WxMaService;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.GifTemplateEntity;
import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.service.GifTemplateService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 动图模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
@RestController
@RequestMapping("/api/giftemplate")
@Api("动图")
public class ApiGifTemplateController {
	
	@Autowired
	private GifTemplateService gifTemplateService;
	@Autowired
	private YykjProperties yykjProperties;

	@IgnoreAuth
	@ApiOperation(value = "动图模板列表", notes = "动图模板分页列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false) })
	@PostMapping("/list")
	public ApiResult list(@ApiIgnore() @RequestParam Map<String, Object> params) {
		params.put("isRelease", SystemConstant.T_STR);
		Query query = new Query(params);
		List<GifTemplateEntity> gifList = gifTemplateService.queryList(query);
		gifList.forEach(g -> {
			if (StringUtils.isNotBlank(g.getCover()) && !g.getCover().startsWith("http")) {
				g.setCover(yykjProperties.getVisitprefix().concat(g.getCover()));
			}
			if (StringUtils.isNotBlank(g.getExamplePath()) && !g.getExamplePath().startsWith("http")) {
				g.setExamplePath(yykjProperties.getVisitprefix().concat(g.getExamplePath()));
			}
		});
		return ApiResult.ok(gifList);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "动态模板详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "动态模板ID", required = true)
			})
	@PostMapping("/detail")
	public ApiResult detail(@RequestParam Integer id){
		GifTemplateEntity gifTemplateEntity = gifTemplateService.queryObject(id);
		Assert.isNullApi(gifTemplateEntity, "该模板不存在");
		if(StringUtils.isNotBlank(gifTemplateEntity.getCover())) {
			gifTemplateEntity.setCover(yykjProperties.getVisitprefix().concat(gifTemplateEntity.getCover()));
		}
		if(StringUtils.isNotBlank(gifTemplateEntity.getExamplePath())) {
			gifTemplateEntity.setExamplePath(yykjProperties.getVisitprefix().concat(gifTemplateEntity.getExamplePath()));
		}
		return ApiResult.ok(gifTemplateEntity);
	}

	@IgnoreAuth
	@ApiOperation(value = "生成动态图", notes = "生成动态图并且返回是否点赞")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "动图ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "sentences", value = "语句(每条语句,相隔)", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "用户ID", required = true), })
	@PostMapping("/generate")
	public ApiResult generate(@RequestParam Integer id, @RequestParam String sentences, @RequestParam String openid) throws Exception {
		String[] sentence = sentences.split(",");
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		for (String sen : sentence) {
			//检测敏感词汇
			/*boolean chechMsg = ContentSecurityKit.chechMsg(accessTokenComponent.getAccessToken(), sen);
			if(chechMsg) {
				return ApiResult.error(500, sen + "包含敏感词");
			}*/
			if (!wxMaService.getSecCheckService().checkMessage(sen)) {
				return ApiResult.error(500, sen + "包含敏感词");
			}
		}
		UseRecordEntity useRecordEntity = gifTemplateService.generate(id, sentences, openid);
		useRecordEntity.setTemplateImageResult(yykjProperties.getVisitprefix().concat(useRecordEntity.getTemplateImageResult()));
		return ApiResult.ok(useRecordEntity);
	}

}
