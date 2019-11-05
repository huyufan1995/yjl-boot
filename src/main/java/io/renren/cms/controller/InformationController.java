package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.InformationsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.service.InformationService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 10:36:31
 */
@RestController
@RequestMapping("information")
public class InformationController {
	@Autowired
	private InformationService informationService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("information:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<InformationsEntity> informationList = informationService.queryList(query);
		int total = informationService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(informationList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("information:info")
	public R info(@PathVariable("id") Integer id){
		InformationsEntity information = informationService.queryObject(id);
		
		return R.ok().put("information", information);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("information:save")
	public R save(@RequestBody InformationsEntity information){
		informationService.save(information);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("information:update")
	public R update(@RequestBody InformationsEntity information){
		informationService.update(information);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("information:delete")
	public R delete(@RequestBody Integer[] ids){
		informationService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("information:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		informationService.logicDel(id);
		return R.ok();
	}
	
}
