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

import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ErrorResponse;
import io.renren.cms.entity.PraiseRecordEntity;
import io.renren.cms.entity.SortEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.service.PraiseRecordService;
import io.renren.cms.service.SortService;
import io.renren.cms.service.TemplateService;
import io.renren.properties.YykjProperties;
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
 * 点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-29 16:03:00
 */
@RestController
@RequestMapping("/api/praiserecord")
@Api("点赞管理")
public class ApiPraiseRecordController {
	@Autowired
	private PraiseRecordService praiseRecordService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private SortService sortService;
	@Autowired
	private YykjProperties yykjProperties;

	@IgnoreAuth
	@ApiOperation(value = "点赞推荐模板列表", notes = "点赞推荐模板列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true) })
	@PostMapping("/praiseList")
	public ApiResult praiseList(@ApiIgnore() @RequestParam Map<String, Object> params) {
		String openId = (String) params.get("openId");
		Assert.isBlank(openId, "openId为空");
		List<TemplateEntity> templateList = new ArrayList<>();
		params.put("isRelease", SystemConstant.TRUE_STR);
		SortEntity sortCnt = sortService.queryObjectByType(SystemConstant.PRAISE_SEARCH);//推荐个数
		if (null != sortCnt && null != sortCnt.getSortDays()) {
			params.put("limit", sortCnt.getSortDays());
		} else {
			params.put("limit", SystemConstant.DEFAULT_PRAISE_CNT);//默认10条
		}

		SortEntity sortDay = sortService.queryObjectByType(SystemConstant.SORT_PRAISE);//推荐天数
		if (null != sortDay && null != sortDay.getSortDays()) {
			params.put("createTime", DateUtils.getStrDateBefore(new Date(), sortDay.getSortDays()));
		} else {
			params.put("createTime", DateUtils.getStrDateBefore(new Date(), SystemConstant.DEFAULT_PRAISE_DAYS));//默认10天
		}
		params.put("offset", SystemConstant.DEFAULT_OFFSET);
		Query query = new Query(params);
		templateList = templateService.queryPraiseListApi(query);//使用量 desc
		if (null != templateList && templateList.size() > 0) {
			Map<String, Object> map = new HashMap<>();
			if (null != sortDay && null != sortDay.getSortDays()) {
				map.put("createTime", DateUtils.getStrDateBefore(new Date(), sortDay.getSortDays()));
			} else {
				map.put("createTime", DateUtils.getStrDateBefore(new Date(), SystemConstant.DEFAULT_PRAISE_DAYS));//默认10天
			}
			for (int i = 0; i < templateList.size(); i++) {
				templateList.get(i).setSortNum(i + 1);
				templateList.get(i)
						.setImageExample(yykjProperties.getVisitprefix().concat(templateList.get(i).getImageExample()));
				templateList.get(i).setImageTemplate(
						yykjProperties.getVisitprefix().concat(templateList.get(i).getImageTemplate()));
				//				templateList.get(i).setPraiseCnt(praiseCnt);
				map.clear();
				map.put("openId", openId);
				map.put("templateId", templateList.get(i).getId());
				boolean p = praiseRecordService.ifPraise(map);
				if (p) {
					templateList.get(i).setIfPraise("t");
				} else {
					templateList.get(i).setIfPraise("f");
				}
				map.put("limit", SystemConstant.DEFAULT_HEADER_CNT);
				map.put("offset", SystemConstant.DEFAULT_OFFSET);
				List<PraiseRecordEntity> praiseList = praiseRecordService.queryPraiseList(map);
				List<String> headerImg = new ArrayList<>();
				for (PraiseRecordEntity praise : praiseList) {
					headerImg.add(praise.getAvatarUrl());
				}
				templateList.get(i).setHeaderImg(headerImg);
			}
		}
		return ApiResult.ok(templateList);
	}

	@IgnoreAuth
	@ApiOperation(value = "查询点赞模板信息", notes = "查询点赞模板信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "templateId", value = "模板id", required = true) })
	@PostMapping("/query_praise_template")
	public ApiResult queryPaiseTemplate(@ApiIgnore() @RequestParam Map<String, Object> params) {
		String openId = (String) params.get("openId");
		Assert.isBlank(openId, "openId为空");
		Integer templateId = Integer.parseInt((String) params.get("templateId"));
		Assert.isNull(templateId, "templateId为空");
		SortEntity sortDay = sortService.queryObjectByType(SystemConstant.SORT_PRAISE);//推荐天数
		if (null != sortDay && null != sortDay.getSortDays()) {
			params.put("createTime", DateUtils.getStrDateBefore(new Date(), sortDay.getSortDays()));
		} else {
			params.put("createTime", DateUtils.getStrDateBefore(new Date(), SystemConstant.DEFAULT_PRAISE_DAYS));//默认10天
		}
		TemplateEntity template = templateService.queryPraiseInfo(params);
		params.put("openId", openId);
		boolean p = praiseRecordService.ifPraise(params);
		if (p) {
			template.setIfPraise("t");
		} else {
			template.setIfPraise("f");
		}

		List<PraiseRecordEntity> praiseList = praiseRecordService.queryPraiseList(params);
		List<String> headerImg = new ArrayList<>();
		for (PraiseRecordEntity praise : praiseList) {
			headerImg.add(praise.getAvatarUrl());
		}
		template.setHeaderImg(headerImg);
		return ApiResult.ok(template);
	}

	@IgnoreAuth
	@ApiOperation(value = "点赞模板", notes = "点赞模板")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "templateId", value = "模板id", required = true) })
	@PostMapping("/praise_template")
	public ApiResult praiseTemplate(@ApiIgnore() @RequestParam Map<String, Object> params) {
		String openId = (String) params.get("openId");
		Assert.isBlank(openId, "openId为空");
		Integer templateId = Integer.parseInt((String) params.get("templateId"));
		Assert.isNull(templateId, "templateId为空");

		//查询是否点过赞
		PraiseRecordEntity p = praiseRecordService.queryObjectByMap(params);
		if (null != p) {
			//点过赞
			if (StringUtils.equals(p.getPraiseStatus(), SystemConstant.PRAISE_SUCCESS)) {
				//点赞状态为成功点赞
				return ErrorResponse.PRAISE_ALREAD;
			} else {
				//点赞状态为取消点赞，修改为点赞状态
				p.setPraiseStatus(SystemConstant.PRAISE_SUCCESS);
				p.setCreateTime(new Date());
				praiseRecordService.update(p);
			}
		} else {
			//保存点赞记录
			PraiseRecordEntity praise = new PraiseRecordEntity();
			praise.setCreateTime(new Date());
			praise.setOpenId(openId);
			praise.setPraiseStatus(SystemConstant.PRAISE_SUCCESS);
			praise.setTemplateId(templateId);

			praiseRecordService.save(praise);
		}

		//更新点赞次数
		TemplateEntity templateEntity = templateService.queryObject(templateId);
		Integer praiseCnt = null != templateEntity.getPraiseCnt() ? templateEntity.getPraiseCnt() : 0;
		templateEntity.setPraiseCnt(praiseCnt + 1);
		templateService.update(templateEntity);

		return ApiResult.ok();
	}

	@IgnoreAuth
	@ApiOperation(value = "取消点赞模板", notes = "取消点赞模板")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "Integer", name = "templateId", value = "模板id", required = true) })
	@PostMapping("/cancel_praise_template")
	public ApiResult cancelPraiseTemplate(@ApiIgnore() @RequestParam Map<String, Object> params) {
		String openId = (String) params.get("openId");
		Assert.isBlank(openId, "openId为空");
		Integer templateId = Integer.parseInt((String) params.get("templateId"));
		Assert.isNull(templateId, "templateId为空");

		//查询是否点过赞
		PraiseRecordEntity praise = praiseRecordService.queryObjectByMap(params);
		if (null != praise) {
			if (StringUtils.equals(praise.getPraiseStatus(), SystemConstant.PRAISE_CANCEL)) {
				//已经取消点赞
				return ErrorResponse.PRAISE_CANCEL;
			} else {
				praise.setPraiseStatus(SystemConstant.PRAISE_CANCEL);
				praise.setCreateTime(new Date());
			}
		} else {
			//没点过赞，无法取消点赞
			return ErrorResponse.PRAISE_NO_EXIST;
		}
		//取消点赞记录
		praiseRecordService.changePraiseStatus(praise);

		//更新点赞次数
		TemplateEntity templateEntity = templateService.queryObject(templateId);
		Integer praiseCnt = null != templateEntity.getPraiseCnt() ? templateEntity.getPraiseCnt() : 0;
		templateEntity.setPraiseCnt(praiseCnt - 1 < 0 ? 0 : praiseCnt - 1);
		templateService.update(templateEntity);

		return ApiResult.ok();
	}
}
