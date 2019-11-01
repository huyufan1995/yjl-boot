package io.renren.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardEntityVo;
import io.renren.cms.entity.CardHolderEntity;
import io.renren.cms.service.CardHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CardEntity;
import io.renren.cms.service.CardService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 名片
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@RestController
@RequestMapping("card")
public class CardController {
	@Autowired
	private CardService cardService;

	@Autowired
	private CardHolderService cardHolderService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("card:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<CardEntityVo> cardList = cardService.queryListVo(query);
		int total = cardService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(cardList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	@RequestMapping("/cardInfoList")
	//@RequiresPermissions("card:list")
	public R cardInfoList(@RequestParam String id) {
		HashMap<String ,Object> params = new HashMap<>();
		params.put("openid",id);
		Query query = new Query(params);
		List<CardHolderEntity> cardHolderList = cardHolderService.queryList(query);
		int total = cardHolderService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(cardHolderList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("card:info")
	public R info(@PathVariable("id") Integer id) {
		CardEntity card = cardService.queryObject(id);

		return R.ok().put("card", card);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("card:save")
	public R save(@RequestBody CardEntity card) {
		cardService.save(card);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("card:update")
	public R update(@RequestBody CardEntity card) {
		cardService.update(card);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("card:delete")
	public R delete(@RequestBody Integer[] ids) {
		cardService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("card:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		cardService.logicDel(id);
		return R.ok();
	}

}
