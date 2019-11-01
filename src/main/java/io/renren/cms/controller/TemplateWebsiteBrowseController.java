package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteBrowseEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TemplateWebsiteBrowseEntity;
import io.renren.cms.service.TemplateWebsiteBrowseService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 官网模板浏览记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-04 10:30:08
 */
@RestController
@RequestMapping("templatewebsitebrowse")
public class TemplateWebsiteBrowseController {
	@Autowired
	private TemplateWebsiteBrowseService templateWebsiteBrowseService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templatewebsitebrowse:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<TemplateWebsiteBrowseEntityVo> templateWebsiteBrowseList = templateWebsiteBrowseService.queryListVo(query);
		int total = templateWebsiteBrowseService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateWebsiteBrowseList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templatewebsitebrowse:info")
	public R info(@PathVariable("id") Integer id){
		TemplateWebsiteBrowseEntity templateWebsiteBrowse = templateWebsiteBrowseService.queryObject(id);
		
		return R.ok().put("templateWebsiteBrowse", templateWebsiteBrowse);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templatewebsitebrowse:save")
	public R save(@RequestBody TemplateWebsiteBrowseEntity templateWebsiteBrowse){
		templateWebsiteBrowseService.save(templateWebsiteBrowse);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templatewebsitebrowse:update")
	public R update(@RequestBody TemplateWebsiteBrowseEntity templateWebsiteBrowse){
		templateWebsiteBrowseService.update(templateWebsiteBrowse);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templatewebsitebrowse:delete")
	public R delete(@RequestBody Integer[] ids){
		templateWebsiteBrowseService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templatewebsitebrowse:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateWebsiteBrowseService.logicDel(id);
		return R.ok();
	}
	
}
