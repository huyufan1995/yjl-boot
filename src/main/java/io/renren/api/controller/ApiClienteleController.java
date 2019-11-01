package io.renren.api.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ClienteleVo;
import io.renren.cms.entity.ClienteleEntity;
import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.cms.entity.ClienteleTagEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.TagEntity;
import io.renren.cms.service.ClienteleAllotRecordService;
import io.renren.cms.service.ClienteleRemarkService;
import io.renren.cms.service.ClienteleService;
import io.renren.cms.service.ClienteleTagService;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.TagService;
import io.renren.enums.ClienteleSourceEnum;
import io.renren.enums.MemberRoleEnum;
import io.renren.utils.ProjectUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 客户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Slf4j
@Api("客户")
@RestController
@RequestMapping("/api/clientele")
public class ApiClienteleController {

	@Autowired
	private ClienteleService clienteleService;
	@Autowired
	private ClienteleRemarkService clienteleRemarkService;
	@Autowired
	private ClienteleTagService clienteleTagService;
	@Autowired
	private TagService tagService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ClienteleAllotRecordService clienteleAllotRecordService;
	
	@PostMapping("/info")
	@ApiOperation(value = "客户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID", required = true)
	})
	public ApiResult info(@RequestParam Integer clienteleId) {
		ClienteleEntity clienteleEntity = clienteleService.queryObject(clienteleId);
		Assert.isNullApi(clienteleEntity, "该客户不存在");
		ClienteleVo clienteleVo = new ClienteleVo();
		BeanUtil.copyProperties(clienteleEntity, clienteleVo);
		if (clienteleEntity.getMemberId() != 0) {
			MemberEntity memberEntity = memberService.queryObject(clienteleVo.getMemberId());
			if (memberEntity != null) {
				//负责人
				clienteleVo.setMemberName(memberEntity.getRealName());
			}
		}
		clienteleVo.setTags(tagService.queryListByClienteleId(clienteleVo.getId()));
		clienteleVo.setRemarks(clienteleRemarkService.queryListByClienteleId(clienteleVo.getId()));
		clienteleVo.setAllotRecord(clienteleAllotRecordService.queryListByClienteleId(clienteleId));
		clienteleVo.setSource(ClienteleSourceEnum.getDes(clienteleEntity.getSource()));
		return ApiResult.ok(clienteleVo);
	}
	
	@PostMapping("/change_member")
	@ApiOperation(value = "更换负责人")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "targetMemberId", value = "目标负责人ID", required = true),
	})
	public ApiResult changeMember(@RequestParam Integer clienteleId, @RequestParam Integer targetMemberId,
			@TokenMember SessionMember sessionMember) {
		//更换负责人并记录更换记录 标签
		clienteleService.changeMember(clienteleId, targetMemberId, sessionMember.getMemberId());
		return ApiResult.ok();
	}
	
	@PostMapping("/search_list")
	@ApiOperation(value = "客户列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司，dept部门", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "allot", value = "是否分配 t:已分配 f:未分配", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "time", value = "日期【非必选，格式2019-01-01】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "tags", value = "标签ID【多个逗号相隔，如1,2,3】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult searchList(
						@RequestParam(value = "type", required = true, defaultValue = "my") String type,
						@RequestParam(value = "allot", required = false, defaultValue = "t") String allot,
						@RequestParam(value = "key", required = false) String key,
						@RequestParam(value = "time", required = false) String time,
						@RequestParam(value = "tags", required = false) String tags,
						@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
						@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
						@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		HashMap<String, Object> params = Maps.newHashMap();
		
		if (StringUtils.isNotBlank(time)) {
			params.put("sdate", time);
			params.put("edate", time);
		}
		
		if (StringUtils.equals(type, "my")) {
			//普通员工
			params.put("memberId", sessionMember.getMemberId());
		} else if (StringUtils.equals(type, "dept")) {
			//部门主管
			params.put("deptId", sessionMember.getDeptId());
		} else if(StringUtils.equals(type, "company")) {
			//管理员
			params.put("superiorId", sessionMember.getSuperiorId());
			if (StringUtils.equals(SystemConstant.F_STR, allot)) {
				//查询未分配客户
				params.put("memberId", 0);
			}
		}
		
		params.put("key", key);
		params.put("tags", tags);
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<ClienteleVo> clienteleVoList = clienteleService.querySearchList(query);
		
		//分配记录
		if (StringUtils.equals(type, "company") || StringUtils.equals(allot, SystemConstant.F_STR)) {
			clienteleVoList.forEach(item -> {
				item.setAllotRecord(clienteleAllotRecordService.queryListByClienteleId(item.getId()));
			});
		}
		
		//客户来源
		clienteleVoList.forEach(item -> {
			item.setSource(ClienteleSourceEnum.getDes(item.getSource()));
		});
		
		return ApiResult.ok(clienteleVoList);
	}
	
	@PostMapping("/member_clientele")
	@ApiOperation(value = "会员客户列表")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "key", value = "手机号/姓名", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "time", value = "日期【非必选，格式2019-01-01】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "tags", value = "标签ID【多个逗号相隔，如1,2,3】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
	})
	public ApiResult memberClientele(@RequestParam Integer memberId,
						@RequestParam(value = "key", required = false) String key,
						@RequestParam(value = "time", required = false) String time,
						@RequestParam(value = "tags", required = false) String tags,
						@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
						@RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit) {

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("memberId", memberId);

		if (StringUtils.isNotBlank(time)) {
			params.put("sdate", time);
			params.put("edate", time);
		}

		params.put("key", key);
		params.put("tags", tags);
		params.put("page", page);
		params.put("limit", limit);
		Query query = new Query(params);
		List<ClienteleVo> clienteleVoList = clienteleService.querySearchList(query);
		clienteleVoList.forEach(item -> {
			item.setAllotRecord(clienteleAllotRecordService.queryListByClienteleId(item.getId()));
			item.setSource(ClienteleSourceEnum.getDes(item.getSource()));
		});

		return ApiResult.ok(clienteleVoList);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "客户【新建客户】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "portrait", value = "头像", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "姓名", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "电话", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "company", value = "公司", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "position", value = "职位", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "tags", value = "标签ID【多个逗号相隔，如1,2,3 没有不传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "remark", value = "备注【没有不传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "source", value = "来源【card智能名片，apply活动表单，manual手工录入，website微官网， cases案例库】", required = false)
	})
	public ApiResult add(@ApiIgnore @Validated ClienteleEntity clienteleEntity, 
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "remark", required = false) String remark,
			@ApiIgnore @TokenMember SessionMember sessionMember
			) {
		if(StringUtils.isBlank(clienteleEntity.getSource())) {
			clienteleEntity.setSource(ClienteleSourceEnum.OTHER.getCode());
		}
		
		if(!Validator.isMobile(clienteleEntity.getMobile())) {
			return ApiResult.error(500, "手机号格式错误");
		}
		clienteleEntity.setMemberId(sessionMember.getMemberId());
		clienteleEntity.setSuperiorId(sessionMember.getSuperiorId());
		clienteleService.add(clienteleEntity, tags, remark);
		return ApiResult.ok(clienteleEntity.getId());
	}
	
	@PostMapping(value = "/import_clientele", headers = "content-type=multipart/form-data")
	@ApiOperation(value = "导入客户")
	public ApiResult importClientele(@ApiParam(value = "数据文件csv,xlsx,xls", required = true) @RequestParam("file") MultipartFile multipartFile,
			@ApiIgnore @TokenMember SessionMember sessionMember) throws IOException {
		
		String fileName = multipartFile.getOriginalFilename();
		String extName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		
		if (StringUtils.equals(".csv", extName)) {
			CsvReader reader = CsvUtil.getReader();
			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			CsvData data = reader.read(tempFile, CharsetUtil.CHARSET_GBK);
			List<CsvRow> rows = data.getRows();
			ClienteleEntity clienteleEntity = null;
			for (int i = 0; i < rows.size(); i++) {
				CsvRow csvRow = rows.get(i);
				// [姓名, 手机, 公司, 职位, 备注]
				List<String> rawList = csvRow.getRawList();
				if(i == 0) {
					if(!ProjectUtils.checkRowTitleStr(rawList)) {
						return ApiResult.error(500, "数据格式错误");
					}
					continue;
				}
				String name = rawList.get(0);
				String mobile = rawList.get(1);
				if(!Validator.isMobile(mobile)) {
					continue;
				}
				if(StringUtils.isBlank(name) || StringUtils.isBlank(mobile)) {
					continue;
				}
				String company = StrUtil.blankToDefault(rawList.get(2), SystemConstant.DEFAULT_CARD_COMPANY);
				String position = StrUtil.blankToDefault(rawList.get(3), SystemConstant.DEFAULT_CARD_POSITION);
				String remark = rawList.get(4);
				
				clienteleEntity = new ClienteleEntity();
				clienteleEntity.setName(name)
					.setMobile(mobile)
					.setCompany(company)
					.setPosition(position)
					.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)))
					.setMemberId(sessionMember.getMemberId())
					.setSuperiorId(sessionMember.getSuperiorId())
					.setSource(ClienteleSourceEnum.IMPORTS.getCode());
				
					try {
						clienteleService.add(clienteleEntity, null, remark);
					} catch (Exception e) {
						log.error("导入客户异常：{}", e.getMessage());
					}
			}

			// 删除临时文件
			File delFile = new File(tempFile.toURI());
			delFile.delete();
		} else if (StringUtils.equals(".xlsx", extName) || StringUtils.equals(".xls", extName)) {
			
			InputStream inputStream = multipartFile.getInputStream();
			File tempFile = new File(multipartFile.getOriginalFilename());
			ProjectUtils.inputStreamToFile(inputStream, tempFile);
			ExcelReader excelReader = ExcelUtil.getReader(tempFile);
			List<List<Object>> rows = excelReader.read();
			ClienteleEntity clienteleEntity = null;
			for (int i = 0; i < rows.size(); i++) {
				List<Object> rawList = rows.get(i);
				// [姓名, 手机, 公司, 职位, 备注]
				if(i == 0) {
					if(!ProjectUtils.checkRowTitleObj(rawList)) {
						return ApiResult.error(500, "数据格式错误");
					}
					continue;
				}
				String name = ProjectUtils.blankListToDefault(rawList, 0, null);
				String mobile = ProjectUtils.blankListToDefault(rawList, 1, null);
				if(!Validator.isMobile(mobile)) {
					continue;
				}
				if(StringUtils.isBlank(name) || StringUtils.isBlank(mobile)) {
					continue;
				}
				String company = ProjectUtils.blankListToDefault(rawList, 2, SystemConstant.DEFAULT_CARD_COMPANY);
				String position = ProjectUtils.blankListToDefault(rawList, 3, SystemConstant.DEFAULT_CARD_POSITION);
				String remark = ProjectUtils.blankListToDefault(rawList, 4, null);
				
				clienteleEntity = new ClienteleEntity();
				clienteleEntity.setName(name)
					.setMobile(mobile)
					.setCompany(company)
					.setPosition(position)
					.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)))
					.setMemberId(sessionMember.getMemberId())
					.setSuperiorId(sessionMember.getSuperiorId())
					.setSource(ClienteleSourceEnum.IMPORTS.getCode());
				
					try {
						clienteleService.add(clienteleEntity, null, remark);
					} catch (Exception e) {
						log.error("导入客户异常：{}", e.getMessage());
					}
			}

			// 删除临时文件
			File delFile = new File(tempFile.toURI());
			delFile.delete();
		
		} else {
			return ApiResult.error(500, "仅支持xlsx,xls,csv数据文件");
		}
		return ApiResult.ok();
	}
	
	@PostMapping("/delete")
	@ApiOperation(value = "删除客户")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID", required = true)
	})
	public ApiResult delete(@RequestParam Integer clienteleId, @ApiIgnore @TokenMember SessionMember sessionMember) {
		if(StringUtils.equals(MemberRoleEnum.BOSS.getCode(), sessionMember.getRole())
				|| StringUtils.equals(MemberRoleEnum.ADMIN.getCode(), sessionMember.getRole())) {
			clienteleService.logicDel(clienteleId);
		} else {
			return ApiResult.error(500, "无权操作");
		}
		return ApiResult.ok();
	}
	
	@PostMapping("/modify")
	@ApiOperation(value = "客户【修改客户基本信息】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "客户", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "姓名【没修改不传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "电话【没修改不传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "company", value = "公司【没修改不传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "position", value = "职位【没修改不传】", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "tags", value = "标签ID【多个逗号相隔，如1,2,3】【没修改不传】", required = false),
	})
	public ApiResult modify(@ApiIgnore @Validated ClienteleEntity clienteleEntity,
			@RequestParam(value = "tags", required = false) String tags) {
		clienteleService.modify(clienteleEntity, tags);
		return ApiResult.ok();
	}

	@Deprecated
	@PostMapping("/tag/add")
	@ApiOperation(value = "标签【新建标签】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "标签名称", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID【选填】", required = false),
	})
	public ApiResult tagAdd(@RequestParam String name, 
			@RequestParam(value = "clienteleId", required = false) Integer clienteleId,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		TagEntity tagEntity = tagService.add(name, clienteleId, sessionMember);
		return ApiResult.ok(tagEntity);
	}
	
	@PostMapping("/tag/label")
	@ApiOperation(value = "标注标签【客户标注标签】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "tagId", value = "标签ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID", required = true)
	})
	public ApiResult tagLabel(@RequestParam Integer tagId, Integer clienteleId,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("clienteleId", clienteleId);
		params.put("tagId", tagId);
		int total = clienteleTagService.queryTotal(params);
		if (total > 0) {
			return ApiResult.error(500, "该客户已标注该标签");
		}
		ClienteleTagEntity clienteleTagEntity = new ClienteleTagEntity();
		clienteleTagEntity.setClienteleId(clienteleId);
		clienteleTagEntity.setMemberId(sessionMember.getMemberId());
		clienteleTagEntity.setTagId(tagId);
		clienteleTagService.save(clienteleTagEntity);
		return ApiResult.ok();
	}
	
	@PostMapping("/v2/tag/add")
	@ApiOperation(value = "标签【新建修改标签V2】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "标签ID", required = false),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "name", value = "标签名称", required = true)
	})
	public ApiResult tagAddV2(@RequestParam(value = "id", required = false) Integer id, @RequestParam String name,
			@ApiIgnore @TokenMember SessionMember sessionMember) {

		if (MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())) {
			return ApiResult.error(500, "无权操作");
		}

		if (id == null) {
			//add
			TagEntity tagEntity = tagService.queryObjectBySuperiorIdAndName(sessionMember.getSuperiorId(), name.trim());
			if (tagEntity == null) {
				tagEntity = new TagEntity();
				tagEntity.setName(name.trim());
				tagEntity.setMemberId(sessionMember.getMemberId());
				tagEntity.setSuperiorId(sessionMember.getSuperiorId());
				tagService.save(tagEntity);
			}
			return ApiResult.ok(tagEntity);
		} else {
			TagEntity tagEntity = new TagEntity();
			tagEntity.setId(id);
			tagEntity.setName(name);
			tagService.update(tagEntity);
			return ApiResult.ok(tagEntity);
		}

	}
	
	@Deprecated
	@PostMapping("/tag/delete")
	@ApiOperation(value = "标签【删除标签】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "tagId", value = "标签ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID【非必传】", required = false)
	})
	public ApiResult tagDelete(@RequestParam Integer tagId, @RequestParam(value = "clienteleId", required = false) Integer clienteleId,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		if(clienteleId != null) {
			clienteleTagService.deleteByClienteleIdAndMemberIdAndTagId(clienteleId, sessionMember.getMemberId(), tagId);
			return ApiResult.ok();
		}
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("tagId", tagId);
		int total = clienteleTagService.queryTotal(params);
		if(total != 0) {
			return ApiResult.error(500, "该标签正在使用，无法删除");
		}
		tagService.delete(tagId);
		return ApiResult.ok();
	}
	
	@PostMapping("/v2/tag/delete")
	@ApiOperation(value = "标签【删除标签V2】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "tagId", value = "标签ID", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID【非必传】", required = false)
	})
	public ApiResult tagDeleteV2(@RequestParam Integer tagId, @RequestParam(value = "clienteleId", required = false) Integer clienteleId,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		
		if(clienteleId != null) {
			//删除客户关联标签关系
			clienteleTagService.deleteByClienteleIdAndTagId(clienteleId, tagId);
			return ApiResult.ok();
		}
		
		if (MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())) {
			return ApiResult.error(500, "无权操作");
		}
		
		tagService.deleteFull(tagId);
		return ApiResult.ok();
	}
	
	@Deprecated
	@PostMapping("/tag/list")
	@ApiOperation(value = "标签【历史标签】")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "type", value = "类型 my我的，company公司", required = false),
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "ownerOpenid", value = "ownerOpenid", required = false)
	})
	public ApiResult tagList(@RequestParam(value = "type", required = true, defaultValue = "my") String type,
			@RequestParam(value = "ownerOpenid", required = false) String ownerOpenid,
			@ApiIgnore @TokenMember SessionMember sessionMember) {
		HashMap<String, Object> params = Maps.newHashMap();
		if(StringUtils.equals("my", type)) {
			if(StringUtils.isNotBlank(ownerOpenid)) {
				MemberEntity memberEntity = memberService.queryObjectByOpenid(ownerOpenid);
				if(memberEntity != null) {
					params.put("memberId", memberEntity.getId());
				}
			} else {
				params.put("memberId", sessionMember.getMemberId());
			}
		} else {
			params.put("superiorId", sessionMember.getSuperiorId());
		}
		List<TagEntity> tagList = tagService.queryList(params);
		return ApiResult.ok(tagList);
	}
	
	@PostMapping("/v2/tag/list")
	@ApiOperation(value = "标签【标签列表V2】")
	public ApiResult tagListV2(@ApiIgnore @TokenMember SessionMember sessionMember) {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("superiorId", sessionMember.getSuperiorId());
		List<TagEntity> tagList = tagService.queryList(params);
		return ApiResult.ok(tagList);
	}
	
	@Deprecated
	@PostMapping("/tag/member_list")
	@ApiOperation(value = "会员标签【历史标签】")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "int", name = "memberId", value = "会员ID", required = true)
	})
	public ApiResult tagMemberList(@RequestParam Integer memberId) {
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("memberId", memberId);
		List<TagEntity> tagList = tagService.queryList(params);
		return ApiResult.ok(tagList);
	}
	
	@PostMapping("/remark/add")
	@ApiOperation(value = "备注【新建】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "content", value = "备注内容", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "clienteleId", value = "客户ID", required = true)
	})
	public ApiResult remarkAdd(@ApiIgnore @Validated ClienteleRemarkEntity clienteleRemarkEntity, @ApiIgnore @TokenMember SessionMember sessionMember) {
		ClienteleEntity clienteleEntity = clienteleService.queryObject(clienteleRemarkEntity.getClienteleId());
		Assert.isNullApi(clienteleEntity, "该客户不存在");
		clienteleRemarkEntity.setCtime(new Date());
		clienteleRemarkEntity.setIsDel(SystemConstant.F_STR);
		clienteleRemarkEntity.setMemberId(sessionMember.getMemberId());
		clienteleRemarkService.save(clienteleRemarkEntity);
		return ApiResult.ok();
	}
	
	@PostMapping("/remark/delete")
	@ApiOperation(value = "备注【删除】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "remarkId", value = "备注ID", required = true)
	})
	public ApiResult remarkDelete(@RequestParam Integer remarkId) {
//		ClienteleRemarkEntity remarkEntity = clienteleRemarkService.queryObject(remarkId);
//		Assert.isNullApi(remarkEntity, "该备注不存在");
		clienteleRemarkService.logicDel(remarkId);
		return ApiResult.ok();
	}
}
