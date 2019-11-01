package io.renren.cms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.service.TemplateService;
import io.renren.properties.YykjProperties;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("template")
public class TemplateController {
	@Autowired
	private TemplateService templateService;
	@Autowired
	private YykjProperties yykjProperties;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TemplateEntity> templateList = templateService.queryList(query);
		
		templateList.forEach(t -> {
			t.setImageExample(yykjProperties.getVisitprefix().concat(t.getImageExample()));
			t.setImageTemplate(yykjProperties.getVisitprefix().concat(t.getImageTemplate()));
		});
		int total = templateService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 精选列表
	 */
	@RequestMapping("/selected_list")
	public R selectedList(@RequestParam Map<String, Object> params){
		//查询列表数据
		params.put("sidx", "praise_cnt");
		params.put("order", "desc");
		Query query = new Query(params);
		
		List<TemplateEntity> templateList = templateService.queryList(query);
		
		templateList.forEach(t -> {
			t.setImageExample(yykjProperties.getVisitprefix().concat(t.getImageExample()));
			t.setImageTemplate(yykjProperties.getVisitprefix().concat(t.getImageTemplate()));
		});
		int total = templateService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		TemplateEntity template = templateService.queryObject(id);
		
		return R.ok().put("template", template);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody TemplateEntity template){
		Date now = new Date();
		template.setCtime(now);
		template.setUtime(now);
		template.setIsDel(SystemConstant.FALSE_STR);
		template.setIsFree(SystemConstant.TRUE_STR);
		template.setIsRelease(SystemConstant.FALSE_STR);
		template.setPraiseCnt(0);
		templateService.save(template);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody TemplateEntity template){
		template.setUtime(new Date());
		templateService.update(template);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		templateService.deleteBatch(ids);
		
		return R.ok();
	}
	
	@RequestMapping("/release")
	public R release(@RequestBody Integer[] ids){
		templateService.releaseBatch(ids);
		return R.ok();
	}
	
	@RequestMapping("/cancelRelease")
	public R cancelRelease(@RequestBody Integer[] ids){
		templateService.cancelReleaseBatch(ids);
		return R.ok();
	}
}
