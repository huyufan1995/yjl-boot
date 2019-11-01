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

import io.renren.cms.entity.PraiseRecordEntity;
import io.renren.cms.service.PraiseRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-29 16:03:00
 */
@RestController
@RequestMapping("praiserecord")
public class PraiseRecordController {
	@Autowired
	private PraiseRecordService praiseRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<PraiseRecordEntity> praiseRecordList = praiseRecordService.queryList(query);
		int total = praiseRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(praiseRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/praiseList/{id}")
	public R praiseList(@PathVariable("id") Integer id){
		Map<String, Object> map = new HashMap<>();
		map.put("templateId", id);
		List<PraiseRecordEntity> praiseRecordList = praiseRecordService.queryPraiseList(map);
		
		return R.ok().put("praiseRecordList", praiseRecordList);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		PraiseRecordEntity praiseRecord = praiseRecordService.queryObject(id);
		
		return R.ok().put("praiseRecord", praiseRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody PraiseRecordEntity praiseRecord){
		praiseRecordService.save(praiseRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody PraiseRecordEntity praiseRecord){
		praiseRecordService.update(praiseRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		praiseRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
