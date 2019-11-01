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

import io.renren.cms.entity.CaseBrowseEntity;
import io.renren.cms.service.CaseBrowseService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 案例库浏览记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
@RestController
@RequestMapping("casebrowse")
public class CaseBrowseController {
	@Autowired
	private CaseBrowseService caseBrowseService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("casebrowse:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CaseBrowseEntity> caseBrowseList = caseBrowseService.queryListVo(query);
		int total = caseBrowseService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(caseBrowseList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("casebrowse:info")
	public R info(@PathVariable("id") Integer id){
		CaseBrowseEntity caseBrowse = caseBrowseService.queryObject(id);
		
		return R.ok().put("caseBrowse", caseBrowse);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("casebrowse:save")
	public R save(@RequestBody CaseBrowseEntity caseBrowse){
		caseBrowseService.save(caseBrowse);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("casebrowse:update")
	public R update(@RequestBody CaseBrowseEntity caseBrowse){
		caseBrowseService.update(caseBrowse);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("casebrowse:delete")
	public R delete(@RequestBody Integer[] ids){
		caseBrowseService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("casebrowse:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		caseBrowseService.logicDel(id);
		return R.ok();
	}
	
}
