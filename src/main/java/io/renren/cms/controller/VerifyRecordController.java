package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.VerifyRecordEntity;
import io.renren.cms.service.VerifyRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 核销记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:37
 */
@RestController
@RequestMapping("verifyrecord")
public class VerifyRecordController {
	@Autowired
	private VerifyRecordService verifyRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("verifyrecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<VerifyRecordEntity> verifyRecordList = verifyRecordService.queryList(query);
		int total = verifyRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(verifyRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("verifyrecord:info")
	public R info(@PathVariable("id") Integer id){
		VerifyRecordEntity verifyRecord = verifyRecordService.queryObject(id);
		
		return R.ok().put("verifyRecord", verifyRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("verifyrecord:save")
	public R save(@RequestBody VerifyRecordEntity verifyRecord){
		verifyRecordService.save(verifyRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("verifyrecord:update")
	public R update(@RequestBody VerifyRecordEntity verifyRecord){
		verifyRecordService.update(verifyRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("verifyrecord:delete")
	public R delete(@RequestBody Integer[] ids){
		verifyRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("verifyrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		verifyRecordService.logicDel(id);
		return R.ok();
	}
	
}
