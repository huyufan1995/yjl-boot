package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.MemberRemovalRecordEntity;
import io.renren.cms.service.MemberRemovalRecordService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 会员迁移记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-06 18:36:05
 */
@RestController
@RequestMapping("memberremovalrecord")
public class MemberRemovalRecordController {
	@Autowired
	private MemberRemovalRecordService memberRemovalRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("memberremovalrecord:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<MemberRemovalRecordEntity> memberRemovalRecordList = memberRemovalRecordService.queryList(query);
		int total = memberRemovalRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(memberRemovalRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("memberremovalrecord:info")
	public R info(@PathVariable("id") Integer id){
		MemberRemovalRecordEntity memberRemovalRecord = memberRemovalRecordService.queryObject(id);
		
		return R.ok().put("memberRemovalRecord", memberRemovalRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("memberremovalrecord:save")
	public R save(@RequestBody MemberRemovalRecordEntity memberRemovalRecord){
		memberRemovalRecordService.save(memberRemovalRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("memberremovalrecord:update")
	public R update(@RequestBody MemberRemovalRecordEntity memberRemovalRecord){
		memberRemovalRecordService.update(memberRemovalRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("memberremovalrecord:delete")
	public R delete(@RequestBody Integer[] ids){
		memberRemovalRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("memberremovalrecord:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		memberRemovalRecordService.logicDel(id);
		return R.ok();
	}
	
}
