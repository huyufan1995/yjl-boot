package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ApplyRecordEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 报名记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
@RestController
@RequestMapping("applyrecord")
public class ApplyRecordController {
	@Autowired
	private ApplyRecordService applyRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("applyrecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<ApplyRecordEntityVo> applyRecordList = applyRecordService.queryListVo(query);
		int total = applyRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("applyrecord:info")
	public R info(@PathVariable("id") Integer id){
		ApplyRecordEntity applyRecord = applyRecordService.queryObject(id);
		
		return R.ok().put("applyRecord", applyRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("applyrecord:save")
	public R save(@RequestBody ApplyRecordEntity applyRecord){
		applyRecordService.save(applyRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("applyrecord:update")
	public R update(@RequestBody ApplyRecordEntity applyRecord){
		applyRecordService.update(applyRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("applyrecord:delete")
	public R delete(@RequestBody Integer[] ids){
		applyRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logicdel/{id}")
	//@RequiresPermissions("applyrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyRecordService.logicDel(id);
		return R.ok();
	}
	
}
