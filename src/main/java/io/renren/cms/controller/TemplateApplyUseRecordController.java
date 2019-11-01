package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TemplateApplyUseRecordEntity;
import io.renren.cms.service.TemplateApplyUseRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 报名模板使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 17:54:54
 */
@RestController
@RequestMapping("templateapplyuserecord")
public class TemplateApplyUseRecordController {
	@Autowired
	private TemplateApplyUseRecordService templateApplyUseRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templateapplyuserecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<TemplateApplyUseRecordEntity> templateApplyUseRecordList = templateApplyUseRecordService.queryList(query);
		int total = templateApplyUseRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateApplyUseRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templateapplyuserecord:info")
	public R info(@PathVariable("id") Integer id){
		TemplateApplyUseRecordEntity templateApplyUseRecord = templateApplyUseRecordService.queryObject(id);
		
		return R.ok().put("templateApplyUseRecord", templateApplyUseRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templateapplyuserecord:save")
	public R save(@RequestBody TemplateApplyUseRecordEntity templateApplyUseRecord){
		templateApplyUseRecordService.save(templateApplyUseRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templateapplyuserecord:update")
	public R update(@RequestBody TemplateApplyUseRecordEntity templateApplyUseRecord){
		templateApplyUseRecordService.update(templateApplyUseRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templateapplyuserecord:delete")
	public R delete(@RequestBody Integer[] ids){
		templateApplyUseRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templateapplyuserecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateApplyUseRecordService.logicDel(id);
		return R.ok();
	}
	
}
