package io.renren.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.CategoryEntity;
import io.renren.cms.service.CategoryService;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 分类
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("/api/category")
@Api("模板分类")
public class ApiCategoryController {

	@Autowired
	private CategoryService categoryService;

	@IgnoreAuth
	@ApiOperation(value = "模板全部分类")
	@PostMapping("/list")
	public ApiResult list() {
		List<CategoryEntity> list = categoryService.queryList(null);//sort_num asc
		return ApiResult.ok(list);
	}

}
