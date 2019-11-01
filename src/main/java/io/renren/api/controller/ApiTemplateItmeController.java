package io.renren.api.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.TemplateItmesForm;
import io.renren.cms.entity.GifTemplateEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.service.GifTemplateService;
import io.renren.cms.service.TemplateItmeService;
import io.renren.cms.service.TemplateService;
import io.renren.cms.service.UseRecordService;
import io.renren.properties.YykjProperties;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 模板项
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("/api/templateitme")
@Api("模板项")
public class ApiTemplateItmeController {
	
	@Autowired
	private TemplateItmeService templateItmeService;
	@Autowired
	private GifTemplateService gifTemplateService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UseRecordService useRecordService ;
	@Autowired
	private YykjProperties yykjProperties;
	
	@IgnoreAuth
	@ApiOperation(value = "模板项明细", notes = "模板所有配置")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "templateId", value = "模板ID", required = true)
			})
	@PostMapping("/detail")
	public ApiResult detail(@RequestParam(value = "templateId", required = true) Integer templateId){
		TemplateEntity templateEntity = templateService.queryObject(templateId);
		Assert.isNullApi(templateEntity, "该模板不存在");
		List<TemplateItmeEntity> templateItmeList = templateItmeService.queryListByTemplateId(templateId);// sort_num asc
		
		templateItmeList.forEach(t -> {
			if(SystemConstant.FONT_STR.equals(t.getType())){
				if(StringUtils.isNotBlank(t.getDescribe())){
					if(SystemConstant.TRUE_STR.equals(t.getIsMultiLine())){
						//多行
						String[] split = t.getDescribe().split("∫");
						t.setFontLength(split[0].length());
					}else{
						t.setFontLength(t.getDescribe().length());
					}
				}
			}else if(SystemConstant.IMAGE_STR.equals(t.getType())){
				if(StringUtils.isNotBlank(t.getImagePath())){
					t.setImagePath(yykjProperties.getVisitprefix().concat(t.getImagePath()));
				}
			}
		});
		templateEntity.setViewCnt(templateEntity.getViewCnt() + 1);
		templateService.update(templateEntity);
		return ApiResult.ok(templateItmeList);
	}
	
	@IgnoreAuth
	@PostMapping("/use")
	@ApiOperation(value = "使用模板", notes = "使用模板，生成图片")
	public ApiResult use(@RequestBody TemplateItmesForm templateItmesForm) {
		Assert.isNullApi(templateItmesForm, "参数不能为空");
		Assert.isBlankApi(templateItmesForm.getOpenid(), "用户ID不能为空");
		Assert.isNullApi(templateItmesForm.getItems(), "参数项不能为空");
		UseRecordEntity useRecordEntity = templateItmeService.use(templateItmesForm);
		useRecordEntity.setTemplateImageResult(yykjProperties.getVisitprefix().concat(useRecordEntity.getTemplateImageResult()));
		return ApiResult.ok(useRecordEntity);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "模板使用记录", notes = "模板使用记录")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "useId", value = "生成模板使用记录ID", required = true)
			})
	@PostMapping("/useInfo")
	public ApiResult useInfo(@RequestParam(value = "useId", required = true) Integer useId){
		UseRecordEntity useRecordEntity = useRecordService.queryObject(useId);
		Assert.isNullApi(useRecordEntity, "使用记录不存在");
		if(StringUtils.equals("static", useRecordEntity.getType())) {
			//静态
			TemplateEntity templateEntity = templateService.queryObject(useRecordEntity.getTemplateId());
			useRecordEntity.setTemplateImageResult(yykjProperties.getVisitprefix().concat(useRecordEntity.getTemplateImageResult()));
			useRecordEntity.setTemplateUrl(yykjProperties.getVisitprefix().concat(templateEntity.getImageTemplate()));
		}else {
			//动态
			GifTemplateEntity gifTemplateEntity = gifTemplateService.queryObject(useRecordEntity.getTemplateId());
			useRecordEntity.setTemplateImageResult(yykjProperties.getVisitprefix().concat(useRecordEntity.getTemplateImageResult()));
			useRecordEntity.setTemplateUrl(yykjProperties.getVisitprefix().concat(gifTemplateEntity.getExamplePath()));
		}
		
		return ApiResult.ok(useRecordEntity);
	}
	
}
