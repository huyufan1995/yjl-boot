package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.PraiseRecordGifEntity;
import io.renren.cms.service.PraiseRecordGifService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 动图点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
@RestController
@RequestMapping("praiserecordgif")
public class PraiseRecordGifController {
	@Autowired
	private PraiseRecordGifService praiseRecordGifService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);

		List<PraiseRecordGifEntity> praiseRecordGifList = praiseRecordGifService.queryList(query);
		int total = praiseRecordGifService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(praiseRecordGifList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id) {
		PraiseRecordGifEntity praiseRecordGif = praiseRecordGifService.queryObject(id);

		return R.ok().put("praiseRecordGif", praiseRecordGif);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody PraiseRecordGifEntity praiseRecordGif) {
		praiseRecordGifService.save(praiseRecordGif);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody PraiseRecordGifEntity praiseRecordGif) {
		praiseRecordGifService.update(praiseRecordGif);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids) {
		praiseRecordGifService.deleteBatch(ids);

		return R.ok();
	}

}
