package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TagEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TagEntity;
import io.renren.cms.service.TagService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 标签
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@RestController
@RequestMapping("tag")
public class TagController {
	@Autowired
	private TagService tagService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("tag:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<TagEntityVo> tagList = tagService.queryListVo(query);
		int total = tagService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(tagList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("tag:info")
	public R info(@PathVariable("id") Integer id) {
		TagEntity tag = tagService.queryObject(id);

		return R.ok().put("tag", tag);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("tag:save")
	public R save(@RequestBody TagEntity tag) {
		tagService.save(tag);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("tag:update")
	public R update(@RequestBody TagEntity tag) {
		tagService.update(tag);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("tag:delete")
	public R delete(@RequestBody Integer[] ids) {
		tagService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("tag:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		tagService.logicDel(id);
		return R.ok();
	}

}
