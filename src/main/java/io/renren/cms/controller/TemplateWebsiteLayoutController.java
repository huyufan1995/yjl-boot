package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteLayoutEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TemplateWebsiteLayoutEntity;
import io.renren.cms.service.TemplateWebsiteLayoutService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 官网模板布局
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 17:50:05
 */
@RestController
@RequestMapping("templatewebsitelayout")
public class TemplateWebsiteLayoutController {
	@Autowired
	private TemplateWebsiteLayoutService templateWebsiteLayoutService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templatewebsitelayout:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<TemplateWebsiteLayoutEntityVo> templateWebsiteLayoutList = templateWebsiteLayoutService.queryListVo(query);
		int total = templateWebsiteLayoutService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateWebsiteLayoutList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templatewebsitelayout:info")
	public R info(@PathVariable("id") Integer id){
		TemplateWebsiteLayoutEntity templateWebsiteLayout = templateWebsiteLayoutService.queryObject(id);
		
		return R.ok().put("templateWebsiteLayout", templateWebsiteLayout);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templatewebsitelayout:save")
	public R save(@RequestBody TemplateWebsiteLayoutEntity templateWebsiteLayout){
		templateWebsiteLayoutService.save(templateWebsiteLayout);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templatewebsitelayout:update")
	public R update(@RequestBody TemplateWebsiteLayoutEntity templateWebsiteLayout){
		templateWebsiteLayoutService.update(templateWebsiteLayout);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templatewebsitelayout:delete")
	public R delete(@RequestBody Integer[] ids){
		templateWebsiteLayoutService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templatewebsitelayout:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateWebsiteLayoutService.logicDel(id);
		return R.ok();
	}
	
}
