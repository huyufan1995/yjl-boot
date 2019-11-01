package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.GroupEntity;
import io.renren.cms.service.GroupService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:45
 */
@RestController
@RequestMapping("group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<GroupEntity> groupList = groupService.queryList(query);
		int total = groupService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(groupList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		GroupEntity group = groupService.queryObject(id);
		
		return R.ok().put("group", group);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody GroupEntity group){
		groupService.save(group);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody GroupEntity group){
		groupService.update(group);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		groupService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
