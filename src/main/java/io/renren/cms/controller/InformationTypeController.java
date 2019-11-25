package io.renren.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.cms.service.InformationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.InformationTypeEntity;
import io.renren.cms.service.InformationTypeService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-07 17:22:16
 */
@RestController
@RequestMapping("informationstype")
public class InformationTypeController {
	@Autowired
	private InformationTypeService informationTypeService;

	@Autowired
	private InformationService informationService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("informationtype:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<InformationTypeEntity> informationTypeList = informationTypeService.queryList(query);
		int total = informationTypeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(informationTypeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("informationtype:info")
	public R info(@PathVariable("id") Integer id){
		InformationTypeEntity informationType = informationTypeService.queryObject(id);
		
		return R.ok().put("informationType", informationType);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("informationtype:save")
	public R save(@RequestBody InformationTypeEntity informationType){
		informationTypeService.save(informationType);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("informationtype:update")
	public R update(@RequestBody InformationTypeEntity informationType){
		informationTypeService.update(informationType);
		
		return R.ok();
	}
	
/*	*//**
	 * 删除
	 *//*
	@RequestMapping("/delete")
	//@RequiresPermissions("informationtype:delete")
	public R delete(@RequestBody Integer[] ids){
		informationTypeService.deleteBatch(ids);
		
		return R.ok();
	}*/
	
	/**
	 * 删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("informationtype:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		HashMap<String,Object> params = new HashMap<>(1);
		params.put("informationType",id);
		int total = informationService.queryTotal(params);
		if(total> 0){
			return R.error("该类别下面有资讯信息，请先删除资讯信息");
		}
		informationTypeService.delete(id);
		return R.ok();
	}

	/**
	 * 查询所以资讯类别名称
	 */
	@RequestMapping("/queryAll")
	//@RequiresPermissions("informationtype:delete")
	public R queryAll(){
		List<InformationTypeEntity> informationTypeList = informationTypeService.queryList(null);

		return R.ok().put("informationTypeList", informationTypeList);
	}
}
