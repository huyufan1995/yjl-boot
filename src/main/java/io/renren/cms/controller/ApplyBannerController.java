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

import io.renren.cms.entity.ApplyBannerEntity;
import io.renren.cms.service.ApplyBannerService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-25 10:59:15
 */
@RestController
@RequestMapping("applybanner")
public class ApplyBannerController {
	@Autowired
	private ApplyBannerService applyBannerService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("applybanner:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<ApplyBannerEntity> applyBannerList = applyBannerService.queryList(query);
		int total = applyBannerService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyBannerList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("applybanner:info")
	public R info(@PathVariable("id") Integer id){
		ApplyBannerEntity applyBanner = applyBannerService.queryObject(id);
		
		return R.ok().put("applyBanner", applyBanner);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("applybanner:save")
	public R save(@RequestBody ApplyBannerEntity applyBanner){
		applyBannerService.save(applyBanner);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("applybanner:update")
	public R update(@RequestBody ApplyBannerEntity applyBanner){
		applyBannerService.update(applyBanner);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("applybanner:delete")
	public R delete(@RequestBody Integer[] ids){
		applyBannerService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("applybanner:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyBannerService.delete(id);
		return R.ok();
	}
	
}
