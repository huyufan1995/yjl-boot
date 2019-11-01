package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.GifTemplateEntity;
import io.renren.cms.service.GifTemplateService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 动图模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
@RestController
@RequestMapping("giftemplate")
public class GifTemplateController {
	@Autowired
	private GifTemplateService gifTemplateService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<GifTemplateEntity> gifTemplateList = gifTemplateService.queryList(query);
		int total = gifTemplateService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(gifTemplateList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id) {
		GifTemplateEntity gifTemplate = gifTemplateService.queryObject(id);

		return R.ok().put("gifTemplate", gifTemplate);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody GifTemplateEntity gifTemplate) {
		gifTemplateService.save(gifTemplate);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody GifTemplateEntity gifTemplate) {
		gifTemplateService.update(gifTemplate);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids) {
		gifTemplateService.deleteBatch(ids);

		return R.ok();
	}

}
