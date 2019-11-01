package io.renren.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.renren.api.constant.SystemConstant;
import io.renren.api.kit.WeixinAppKit;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ErrorResponse;
import io.renren.cms.entity.GroupEntity;
import io.renren.cms.entity.GroupRecordEntity;
import io.renren.cms.entity.SortEntity;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.UserDaysEntity;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.GroupRecordService;
import io.renren.cms.service.GroupService;
import io.renren.cms.service.SortService;
import io.renren.cms.service.TemplateService;
import io.renren.cms.service.UseRecordService;
import io.renren.cms.service.UserDaysService;
import io.renren.cms.service.WxUserService;
import io.renren.properties.YykjProperties;
import io.renren.utils.DateUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("/api/template")
@Api("模板")
public class ApiTemplateController {

	private Logger logger = LoggerFactory.getLogger(ApiTemplateController.class);
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UseRecordService useRecordService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupRecordService groupRecordService;
	@Autowired
	private UserDaysService userDaysService;
	@Autowired
	private SortService sortService;
	@Autowired
	private YykjProperties yykjProperties;

	@IgnoreAuth
	@ApiOperation("模板列表【首页显示】")
	@PostMapping("/list_index")
	public ApiResult listIndex() {
		List<TemplateEntity> templateList = templateService.queryListIndex();
		templateList.forEach(item -> {
			item.setImageExample(yykjProperties.getVisitprefix().concat(item.getImageExample()));
			item.setImageTemplate(yykjProperties.getVisitprefix().concat(item.getImageTemplate()));
		});
		return ApiResult.ok(templateList);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "模板列表【根据类别检索】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "categoryId", value = "分类ID", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "isFun", value = "是否趣图 t是 f否", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "isHot", value = "是否最热 t是 f否", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false) })
	@PostMapping("/list_category")
	public ApiResult listCategory(@ApiIgnore @RequestParam Map<String, Object> params) {
		//已发布的
		params.put("isRelease", SystemConstant.TRUE_STR);
		//最热
		if (params.get("isHot") != null && StringUtils.equals(SystemConstant.T_STR, params.get("isHot").toString())) {
			//按使用量倒叙
			params.put("sidx", "use_cnt");
			params.put("order", "desc");
		}
		Query query = new Query(params);
		List<TemplateEntity> templateList = templateService.queryListApi(query);
		for (TemplateEntity templateEntity : templateList) {
			templateEntity.setUseRecords(useRecordService.queryListByTemplateIdGroup(templateEntity.getId()));//5条使用人
			templateEntity.setImageExample(yykjProperties.getVisitprefix().concat(templateEntity.getImageExample()));
			templateEntity.setImageTemplate(yykjProperties.getVisitprefix().concat(templateEntity.getImageTemplate()));
		}
		return ApiResult.ok(templateList);
	}

	@ApiIgnore
	@IgnoreAuth
	@ApiOperation(value = "模板列表", notes = "模板列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "categoryId", value = "分类ID", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false) })
	@PostMapping("/list")
	public ApiResult list(@ApiIgnore @RequestParam Map<String, Object> params) {

		if (params.get("categoryId") != null && params.get("categoryId").toString().equals("1")) {
			// 全部
			params.remove("categoryId");
		}

		params.put("isRelease", SystemConstant.TRUE_STR);
		// 查询列表数据
		Query query = new Query(params);
		List<TemplateEntity> templateList = templateService.queryListApi(query);// utime
																				// desc

		for (TemplateEntity templateEntity : templateList) {
			templateEntity.setUseRecords(useRecordService.queryListByTemplateId(templateEntity.getId()));// 4条使用人
			templateEntity.setImageExample(yykjProperties.getVisitprefix().concat(templateEntity.getImageExample()));
			templateEntity.setImageTemplate(yykjProperties.getVisitprefix().concat(templateEntity.getImageTemplate()));
		}

		int total = templateService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(templateList, total, query.getLimit(), query.getPage());
		return ApiResult.ok(pageUtil);
	}

	@ApiIgnore
	@IgnoreAuth
	@ApiOperation(value = "首页新模板列表", notes = "首页新模板列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "categoryId", value = "分类ID", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "sortType", value = "类型(all-全部;newest-最新;hotest-最热)", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认10", required = false) })
	@PostMapping("/list_new")
	public ApiResult listNew(@ApiIgnore @RequestParam Map<String, Object> params) {
		//sortType：all-全部；newest-最新；hotest-最热
		String sortType = (String) params.get("sortType");
		Assert.isBlank(sortType, "sortType为空");
		List<TemplateEntity> templateList = new ArrayList<>();
		params.put("isRelease", SystemConstant.TRUE_STR);
		params.put("isFun", SystemConstant.F_STR);
		if (StringUtils.equals(sortType, "all")) {
			//全部

			//			params.put("sidx","id");
			//			params.put("order", "desc");;
			Query query = new Query(params);
			templateList = templateService.queryListApi(query);//utime desc
		} else if (StringUtils.equals(sortType, "hotest")) {
			//最热

			SortEntity sortEntity = sortService.queryObjectByType(SystemConstant.SORT_HOT);//最热排序
			if (Integer.parseInt((String) params.get("page")) <= 10) {
				params.remove("categoryId");
				if (null != sortEntity && null != sortEntity.getSortDays()) {
					params.put("createTime", DateUtils.getStrDateBefore(new Date(), sortEntity.getSortDays()));
				} else {
					params.put("createTime", DateUtils.getStrDateBefore(new Date(), SystemConstant.DEFAULT_SORT_DAYS));//默认7天
				}
				Query query = new Query(params);
				templateList = templateService.queryHotListApi(query);//使用量 desc
			}
		} else {
			//最新
			if (Integer.parseInt((String) params.get("page")) <= 10) {
				params.remove("categoryId");
				params.put("sidx", "id");
				params.put("order", "desc");
				Query query = new Query(params);
				templateList = templateService.queryListApi(query);//id desc
			}
		}
		if (params.get("categoryId") != null && params.get("categoryId").toString().equals("1")) {
			//全部
			params.remove("categoryId");
		}

		// 查询列表数据

		for (TemplateEntity templateEntity : templateList) {
			templateEntity.setUseRecords(useRecordService.queryListByTemplateId(templateEntity.getId()));//4条使用人
			templateEntity.setImageExample(yykjProperties.getVisitprefix().concat(templateEntity.getImageExample()));
			templateEntity.setImageTemplate(yykjProperties.getVisitprefix().concat(templateEntity.getImageTemplate()));
		}

		return ApiResult.ok(templateList);
	}

	@ApiIgnore
	@IgnoreAuth
	@ApiOperation(value = "趣图模板列表", notes = "趣图模板列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认10", required = false) })
	@PostMapping("/fun_list")
	public ApiResult funList(@ApiIgnore @RequestParam Map<String, Object> params) {
		//sortType：all-全部；newest-最新；hotest-最热
		List<TemplateEntity> templateList = new ArrayList<>();
		params.put("isRelease", SystemConstant.TRUE_STR);
		params.put("isFun", SystemConstant.T_STR);
		Query query = new Query(params);
		templateList = templateService.queryListApi(query);//utime desc

		// 查询列表数据
		for (TemplateEntity templateEntity : templateList) {
			templateEntity.setUseRecords(useRecordService.queryListByTemplateId(templateEntity.getId()));//4条使用人
			templateEntity.setImageExample(yykjProperties.getVisitprefix().concat(templateEntity.getImageExample()));
			templateEntity.setImageTemplate(yykjProperties.getVisitprefix().concat(templateEntity.getImageTemplate()));
		}

		return ApiResult.ok(templateList);
	}

	@IgnoreAuth
	@ApiOperation(value = "转发", notes = "转发")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "encryptedData", value = "encryptedData", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "iv", value = "iv", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "type", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openId", value = "openId", required = true) })
	@PostMapping("/template_group")
	public ApiResult templateGroup(@ApiIgnore @RequestParam Map<String, Object> params) {
		String type = (String) params.get("type");
		String openId = (String) params.get("openId");
		Assert.isNull(type, "type不能为空");
		Assert.isNull(openId, "openId不能为空");
		if (StringUtils.equals(type, "personal")) {
			//转发个人
			GroupRecordEntity groupRecord = new GroupRecordEntity();
			groupRecord.setCreateTime(new Date());
			groupRecord.setOpenId(openId);
			groupRecord.setType(type);
			groupRecordService.save(groupRecord);
			return ErrorResponse.OPEN_PERSONAL_HAVE;
		} else {
			//转发群
			String encryptedData = (String) params.get("encryptedData");
			String iv = (String) params.get("iv");
			Assert.isBlankApi(encryptedData, "encryptedData不能为空");
			Assert.isBlankApi(iv, "iv不能为空");

			WxUserEntity wxUser = wxUserService.queryByOpenId(openId);
			Assert.isNullApi(wxUser, "无法获取用户");

			String groupinfo = WeixinAppKit.decrypt(wxUser.getSessionKey(), encryptedData, iv);
			logger.info("解密微信群发信息==>" + groupinfo);
			JSONObject jo = JSON.parseObject(groupinfo);
			if (null == jo) {
				return ErrorResponse.OPEN_GROUP_EXPECTION;
			}
			UserDaysEntity userDays = null;
			Map<String, Object> map = new HashMap<>();
			map.put("openId", openId);
			map.put("groupId", jo.getString("openGId"));
			List<GroupEntity> group = groupService.queryList(map);

			if (null != group && group.size() > 0) {
				GroupRecordEntity groupRecord = new GroupRecordEntity();
				groupRecord.setCreateTime(new Date());
				groupRecord.setOpenId(openId);
				groupRecord.setType(type);
				groupRecordService.save(groupRecord);
				return ErrorResponse.OPEN_GROUP_HAVE;
			} else {
				GroupEntity g = new GroupEntity();
				g.setCreateTime(new Date());
				g.setGroupId(jo.getString("openGId"));
				g.setOpenId(openId);

				groupService.save(g);

				GroupRecordEntity groupRecord = new GroupRecordEntity();
				groupRecord.setCreateTime(new Date());
				groupRecord.setOpenId(openId);
				groupRecord.setType(type);
				groupRecordService.save(groupRecord);

				//增加使用天数相关处理
				userDays = userDaysService.dealUserDays(openId, "send");
			}
			return ApiResult.ok(userDays);
		}
	}

}
