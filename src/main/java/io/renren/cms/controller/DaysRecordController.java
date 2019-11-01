package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.DaysRecordEntity;
import io.renren.cms.service.DaysRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
@RestController
@RequestMapping("daysrecord")
public class DaysRecordController {
	@Autowired
	private DaysRecordService daysRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<DaysRecordEntity> daysRecordList = daysRecordService.queryList(query);
		int total = daysRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(daysRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		DaysRecordEntity daysRecord = daysRecordService.queryObject(id);
		
		return R.ok().put("daysRecord", daysRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody DaysRecordEntity daysRecord){
		daysRecordService.save(daysRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody DaysRecordEntity daysRecord){
		daysRecordService.update(daysRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		daysRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
