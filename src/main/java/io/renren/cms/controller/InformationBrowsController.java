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

import io.renren.cms.entity.InformationBrowsEntity;
import io.renren.cms.service.InformationBrowsService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 资讯浏览记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 14:39:16
 */
@RestController
@RequestMapping("informationbrows")
public class InformationBrowsController {
	@Autowired
	private InformationBrowsService informationBrowsService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("informationbrows:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<InformationBrowsEntity> informationBrowsList = informationBrowsService.queryList(query);
		int total = informationBrowsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(informationBrowsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("informationbrows:info")
	public R info(@PathVariable("id") Integer id){
		InformationBrowsEntity informationBrows = informationBrowsService.queryObject(id);
		
		return R.ok().put("informationBrows", informationBrows);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("informationbrows:save")
	public R save(@RequestBody InformationBrowsEntity informationBrows){
		informationBrowsService.save(informationBrows);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("informationbrows:update")
	public R update(@RequestBody InformationBrowsEntity informationBrows){
		informationBrowsService.update(informationBrows);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("informationbrows:delete")
	public R delete(@RequestBody Integer[] ids){
		informationBrowsService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("informationbrows:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		informationBrowsService.logicDel(id);
		return R.ok();
	}
	
}
