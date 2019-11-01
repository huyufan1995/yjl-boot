package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardHolderEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CardHolderEntity;
import io.renren.cms.service.CardHolderService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 名片夹
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 17:23:12
 */
@RestController
@RequestMapping("cardholder")
public class CardHolderController {
	@Autowired
	private CardHolderService cardHolderService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("cardholder:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<CardHolderEntityVo> cardHolderList = cardHolderService.queryListVo(query);
		int total = cardHolderService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(cardHolderList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("cardholder:info")
	public R info(@PathVariable("id") Integer id) {
		CardHolderEntity cardHolder = cardHolderService.queryObject(id);

		return R.ok().put("cardHolder", cardHolder);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("cardholder:save")
	public R save(@RequestBody CardHolderEntity cardHolder) {
		cardHolderService.save(cardHolder);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("cardholder:update")
	public R update(@RequestBody CardHolderEntity cardHolder) {
		cardHolderService.update(cardHolder);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("cardholder:delete")
	public R delete(@RequestBody Integer[] ids) {
		cardHolderService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("cardholder:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		cardHolderService.logicDel(id);
		return R.ok();
	}

}
