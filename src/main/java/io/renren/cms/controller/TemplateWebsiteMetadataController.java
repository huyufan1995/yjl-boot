package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TemplateWebsiteMetadataEntity;
import io.renren.cms.service.TemplateWebsiteMetadataService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 官网模板元数据
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 16:59:43
 */
@RestController
@RequestMapping("templatewebsitemetadata")
public class TemplateWebsiteMetadataController {
	@Autowired
	private TemplateWebsiteMetadataService templateWebsiteMetadataService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templatewebsitemetadata:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<TemplateWebsiteMetadataEntity> templateWebsiteMetadataList = templateWebsiteMetadataService.queryList(query);
		int total = templateWebsiteMetadataService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateWebsiteMetadataList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templatewebsitemetadata:info")
	public R info(@PathVariable("id") Integer id){
		TemplateWebsiteMetadataEntity templateWebsiteMetadata = templateWebsiteMetadataService.queryObject(id);
		
		return R.ok().put("templateWebsiteMetadata", templateWebsiteMetadata);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templatewebsitemetadata:save")
	public R save(@RequestBody TemplateWebsiteMetadataEntity templateWebsiteMetadata){
		templateWebsiteMetadataService.save(templateWebsiteMetadata);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templatewebsitemetadata:update")
	public R update(@RequestBody TemplateWebsiteMetadataEntity templateWebsiteMetadata){
		templateWebsiteMetadataService.update(templateWebsiteMetadata);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templatewebsitemetadata:delete")
	public R delete(@RequestBody Integer[] ids){
		templateWebsiteMetadataService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templatewebsitemetadata:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateWebsiteMetadataService.logicDel(id);
		return R.ok();
	}
	
}
