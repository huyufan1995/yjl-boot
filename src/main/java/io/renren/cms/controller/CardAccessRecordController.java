package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardAccessRecordEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.cms.service.CardAccessRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 名片访问记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@RestController
@RequestMapping("cardaccessrecord")
public class CardAccessRecordController {
	@Autowired
	private CardAccessRecordService cardAccessRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("cardaccessrecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CardAccessRecordEntityVo> cardAccessRecordList = cardAccessRecordService.queryListVo(query);
		int total = cardAccessRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(cardAccessRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("cardaccessrecord:info")
	public R info(@PathVariable("id") Integer id){
		CardAccessRecordEntity cardAccessRecord = cardAccessRecordService.queryObject(id);
		
		return R.ok().put("cardAccessRecord", cardAccessRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("cardaccessrecord:save")
	public R save(@RequestBody CardAccessRecordEntity cardAccessRecord){
		cardAccessRecordService.save(cardAccessRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("cardaccessrecord:update")
	public R update(@RequestBody CardAccessRecordEntity cardAccessRecord){
		cardAccessRecordService.update(cardAccessRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("cardaccessrecord:delete")
	public R delete(@RequestBody Integer[] ids){
		cardAccessRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("cardaccessrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		cardAccessRecordService.logicDel(id);
		return R.ok();
	}
	
}
