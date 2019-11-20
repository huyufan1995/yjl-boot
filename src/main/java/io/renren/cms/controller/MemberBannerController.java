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

import io.renren.cms.entity.MemberBannerEntity;
import io.renren.cms.service.MemberBannerService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 会员banner
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 11:15:13
 */
@RestController
@RequestMapping("memberbanner")
public class MemberBannerController {
	@Autowired
	private MemberBannerService memberBannerService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("memberbanner:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<MemberBannerEntity> memberBannerList = memberBannerService.queryList(query);
		int total = memberBannerService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(memberBannerList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("memberbanner:info")
	public R info(@PathVariable("id") Integer id){
		MemberBannerEntity memberBanner = memberBannerService.queryObject(id);
		
		return R.ok().put("memberBanner", memberBanner);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("memberbanner:save")
	public R save(@RequestBody MemberBannerEntity memberBanner){
		memberBannerService.save(memberBanner);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("memberbanner:update")
	public R update(@RequestBody MemberBannerEntity memberBanner){
		memberBannerService.update(memberBanner);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("memberbanner:delete")
	public R delete(@RequestBody Integer[] ids){
		memberBannerService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("memberbanner:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		memberBannerService.logicDel(id);
		return R.ok();
	}
	
}
