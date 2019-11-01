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

import io.renren.cms.entity.GroupRecordEntity;
import io.renren.cms.service.GroupRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 转发记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:36
 */
@RestController
@RequestMapping("grouprecord")
public class GroupRecordController {
	@Autowired
	private GroupRecordService groupRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GroupRecordEntity> groupRecordList = groupRecordService.queryList(query);
		int total = groupRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(groupRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		GroupRecordEntity groupRecord = groupRecordService.queryObject(id);
		
		return R.ok().put("groupRecord", groupRecord);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/infoByUser/{type}/{openId}")
	public R infoByUser(@PathVariable("type") String type,@PathVariable("openId") String openId){
		Map<String, Object> map = new HashMap<>();
		map.put("openId", openId);
		map.put("type", type);
		List<GroupRecordEntity>  groupRecord = groupRecordService.queryObjectByParam(map);
		
		return R.ok().put("groupUserRecord", groupRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody GroupRecordEntity groupRecord){
		groupRecordService.save(groupRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody GroupRecordEntity groupRecord){
		groupRecordService.update(groupRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		groupRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
