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

import io.renren.cms.entity.SortEntity;
import io.renren.cms.service.SortService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 首页数据天数
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:37
 */
@RestController
@RequestMapping("sort")
public class SortController {
	@Autowired
	private SortService sortService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SortEntity> sortList = sortService.queryList(query);
		int total = sortService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sortList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		SortEntity sort = sortService.queryObject(id);
		
		return R.ok().put("sort", sort);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody SortEntity sort){
		sortService.save(sort);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody SortEntity sort){
		sort.setUpdateTime(new Date());
		sortService.update(sort);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		sortService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
