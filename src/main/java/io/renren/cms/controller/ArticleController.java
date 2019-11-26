package io.renren.cms.controller;

import io.renren.cms.entity.ArticleEntity;
import io.renren.cms.service.ArticleService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-24 12:27:52
 */
@RestController
@RequestMapping("article")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<ArticleEntity> articleList = articleService.queryList(query);
		int total = articleService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(articleList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id) {
		ArticleEntity article = articleService.queryObject(id);

		return R.ok().put("article", article);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody ArticleEntity article) {
		article.setCtime(new Date());
		articleService.save(article);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody ArticleEntity article) {
		article.setUtime(new Date());
		articleService.update(article);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids) {
		articleService.deleteBatch(ids);
		return R.ok();
	}

}
