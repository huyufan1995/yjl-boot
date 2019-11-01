package io.renren.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.PraiseRecordGifEntity;
import io.renren.cms.service.PraiseRecordGifService;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 动图点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
@RestController
@RequestMapping("/api/praiserecordgif")
@Api("动图-点赞")
public class ApiPraiseRecordGifController {
	@Autowired
	private PraiseRecordGifService praiseRecordGifService;

	@IgnoreAuth
	@PostMapping("/praise")
	@ApiOperation(value = "点赞或者取消点赞")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "动图模板ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "用户ID", required = true) })
	public ApiResult praise(Integer id, String openid) {
		Assert.isNullApi(id, "动图模板ID不能为空");
		Assert.isBlankApi(openid, "用户ID不能为空");
		PraiseRecordGifEntity praiseRecordGifEntity = praiseRecordGifService.queryObjectByTemplateIdAndOpenId(openid, id);
		if (null == praiseRecordGifEntity) {
			PraiseRecordGifEntity temp = new PraiseRecordGifEntity();
			temp.setCtime(new Date());
			temp.setOpenId(openid);
			temp.setTemplateId(id);
			praiseRecordGifService.save(temp);
		} else {
			// 取消收藏
			praiseRecordGifService.delete(praiseRecordGifEntity.getId());
		}
		return ApiResult.ok();
	}

}
