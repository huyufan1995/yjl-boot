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

import io.renren.cms.entity.CollectEntity;
import io.renren.cms.service.CollectService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 收藏
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 17:25:18
 */
@RestController
@RequestMapping("collect")
public class CollectController {
	@Autowired
	private CollectService collectService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("collect:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CollectEntity> collectList = collectService.queryList(query);
		int total = collectService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(collectList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("collect:info")
	public R info(@PathVariable("id") Integer id){
		CollectEntity collect = collectService.queryObject(id);
		
		return R.ok().put("collect", collect);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("collect:save")
	public R save(@RequestBody CollectEntity collect){
		collectService.save(collect);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("collect:update")
	public R update(@RequestBody CollectEntity collect){
		collectService.update(collect);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("collect:delete")
	public R delete(@RequestBody Integer[] ids){
		collectService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("collect:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		collectService.logicDel(id);
		return R.ok();
	}
	
}
