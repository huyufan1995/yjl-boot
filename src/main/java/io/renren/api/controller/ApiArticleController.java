package io.renren.api.controller;


import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.ArticleEntity;
import io.renren.cms.service.ArticleService;
import io.renren.utils.annotation.IgnoreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章
 * 
 * @author moran
 * @date 2019-11-11
 */
@RestController
@RequestMapping("/api/article")
@Api("文章")
public class ApiArticleController {

	@Autowired
	private ArticleService articleService;

	@IgnoreAuth
	@ApiOperation(value = "文章详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "1:关于我们 2:帮助中心", required = true) })
	@PostMapping("/info")
	public ApiResult info(@RequestParam(value = "id", required = true) Integer id) {
		ArticleEntity article = articleService.queryObject(id);
		return ApiResult.ok(article);
	}

}
