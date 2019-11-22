package io.renren.cms.controller;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.service.TemplateItmeService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 模板项
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("templateitme")
public class TemplateItmeController {
	@Autowired
	private TemplateItmeService templateItmeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TemplateItmeEntity> templateItmeList = templateItmeService.queryList(query);
		int total = templateItmeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateItmeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		TemplateItmeEntity templateItme = templateItmeService.queryObject(id);
		
		return R.ok().put("templateItme", templateItme);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody TemplateItmeEntity templateItme){
		Date now = new Date();
		templateItme.setCtime(now);
		templateItme.setUtime(now);
		templateItme.setIsDel(SystemConstant.FALSE_STR);
		templateItmeService.save(templateItme);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody TemplateItmeEntity templateItme){
		templateItme.setUtime(new Date());
		templateItmeService.update(templateItme);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		templateItmeService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
