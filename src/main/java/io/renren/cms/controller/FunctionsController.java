package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.FunctionsEntity;
import io.renren.cms.service.FunctionsService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 功能权益
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-30 15:17:45
 */
@RestController
@RequestMapping("functions")
public class FunctionsController {
	@Autowired
	private FunctionsService functionsService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("functions:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<FunctionsEntity> functionsList = functionsService.queryList(query);
		int total = functionsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(functionsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("functions:info")
	public R info(@PathVariable("id") Integer id){
		FunctionsEntity functions = functionsService.queryObject(id);
		
		return R.ok().put("functions", functions);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("functions:save")
	public R save(@RequestBody FunctionsEntity functions){
		functionsService.save(functions);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("functions:update")
	public R update(@RequestBody FunctionsEntity functions){
		functionsService.update(functions);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("functions:delete")
	public R delete(@RequestBody Integer[] ids){
		functionsService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("functions:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		functionsService.logicDel(id);
		return R.ok();
	}
	
}
