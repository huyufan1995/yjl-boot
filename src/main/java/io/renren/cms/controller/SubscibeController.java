package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.SubscibeService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 订阅
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-06-28 17:06:27
 */
@RestController
@RequestMapping("subscibe")
public class SubscibeController {
	@Autowired
	private SubscibeService subscibeService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<SubscibeEntity> subscibeList = subscibeService.queryList(query);
		int total = subscibeService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(subscibeList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id) {
		SubscibeEntity subscibe = subscibeService.queryObject(id);

		return R.ok().put("subscibe", subscibe);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody SubscibeEntity subscibe) {
		subscibeService.save(subscibe);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody SubscibeEntity subscibe) {
		subscibeService.update(subscibe);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids) {
		subscibeService.deleteBatch(ids);

		return R.ok();
	}

}
