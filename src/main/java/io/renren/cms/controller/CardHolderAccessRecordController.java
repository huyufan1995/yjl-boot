package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CardHolderAccessRecordEntity;
import io.renren.cms.service.CardHolderAccessRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 名片夹访问记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-11 10:50:49
 */
@RestController
@RequestMapping("cardholderaccessrecord")
public class CardHolderAccessRecordController {
	@Autowired
	private CardHolderAccessRecordService cardHolderAccessRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("cardholderaccessrecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CardHolderAccessRecordEntity> cardHolderAccessRecordList = cardHolderAccessRecordService.queryList(query);
		int total = cardHolderAccessRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(cardHolderAccessRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("cardholderaccessrecord:info")
	public R info(@PathVariable("id") Integer id){
		CardHolderAccessRecordEntity cardHolderAccessRecord = cardHolderAccessRecordService.queryObject(id);
		
		return R.ok().put("cardHolderAccessRecord", cardHolderAccessRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("cardholderaccessrecord:save")
	public R save(@RequestBody CardHolderAccessRecordEntity cardHolderAccessRecord){
		cardHolderAccessRecordService.save(cardHolderAccessRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("cardholderaccessrecord:update")
	public R update(@RequestBody CardHolderAccessRecordEntity cardHolderAccessRecord){
		cardHolderAccessRecordService.update(cardHolderAccessRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("cardholderaccessrecord:delete")
	public R delete(@RequestBody Integer[] ids){
		cardHolderAccessRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("cardholderaccessrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		cardHolderAccessRecordService.logicDel(id);
		return R.ok();
	}
	
}
