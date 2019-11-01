package io.renren.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import io.renren.cms.service.TemplateWebsiteUseRecordService;
import io.renren.properties.YykjProperties;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 官网模板使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
@RestController
@RequestMapping("templatewebsiteuserecord")
public class TemplateWebsiteUseRecordController {
	@Autowired
	private TemplateWebsiteUseRecordService templateWebsiteUseRecordService;

	@Autowired
	private YykjProperties yykjProperties;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templatewebsiteuserecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
		List<TemplateWebsiteUseRecordEntity> templateWebsiteUseRecordList = templateWebsiteUseRecordService.queryList(query);
		templateWebsiteUseRecordList.forEach(templateWebsiteUseRecordEntity -> {
			templateWebsiteUseRecordEntity.setTemplateImageExample(yykjProperties.getVisitprefix()+templateWebsiteUseRecordEntity.getTemplateImageExample());
		});
		int total = templateWebsiteUseRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateWebsiteUseRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templatewebsiteuserecord:info")
	public R info(@PathVariable("id") Integer id){
		TemplateWebsiteUseRecordEntity templateWebsiteUseRecord = templateWebsiteUseRecordService.queryObject(id);
		
		return R.ok().put("templateWebsiteUseRecord", templateWebsiteUseRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templatewebsiteuserecord:save")
	public R save(@RequestBody TemplateWebsiteUseRecordEntity templateWebsiteUseRecord){
		templateWebsiteUseRecordService.save(templateWebsiteUseRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templatewebsiteuserecord:update")
	public R update(@RequestBody TemplateWebsiteUseRecordEntity templateWebsiteUseRecord){
		templateWebsiteUseRecordService.update(templateWebsiteUseRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templatewebsiteuserecord:delete")
	public R delete(@RequestBody Integer[] ids){
		templateWebsiteUseRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templatewebsiteuserecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateWebsiteUseRecordService.logicDel(id);
		return R.ok();
	}

	/**
	 * 根据openid查询用户模板使用记录列表
	 */
	@RequestMapping("/listbyopenid")
	//@RequiresPermissions("templatewebsiteuserecord:list")
	public R queryByOpenId(@RequestParam String  openid){
		Map<String,Object> params = new HashMap<>(1);
		params.put("openid",openid);
		Query query = new Query(params);
		List<TemplateWebsiteUseRecordEntity> templateWebsiteUseRecordList = templateWebsiteUseRecordService.queryList(query);
		templateWebsiteUseRecordList.forEach(templateWebsiteUseRecordEntity -> {
			templateWebsiteUseRecordEntity.setTemplateImageExample(yykjProperties.getVisitprefix()+templateWebsiteUseRecordEntity.getTemplateImageExample());
		});
		int total = templateWebsiteUseRecordService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(templateWebsiteUseRecordList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

}
