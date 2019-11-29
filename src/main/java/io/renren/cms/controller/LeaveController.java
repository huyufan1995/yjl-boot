package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.cms.vo.LeaveEntityVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.LeaveEntity;
import io.renren.cms.service.LeaveService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 留言
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 14:32:10
 */
@RestController
@RequestMapping("leave")
public class LeaveController {
	@Autowired
	private LeaveService leaveService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("leave:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<LeaveEntityVO> leaveList = leaveService.queryListVO(query);
		int total = leaveService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(leaveList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("leave:info")
	public R info(@PathVariable("id") Integer id){
		LeaveEntity leave = leaveService.queryObject(id);
		
		return R.ok().put("leave", leave);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("leave:save")
	public R save(@RequestBody LeaveEntity leave){
		leaveService.save(leave);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("leave:update")
	public R update(@RequestBody LeaveEntity leave){
		leaveService.update(leave);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("leave:delete")
	public R delete(@RequestBody Integer[] ids){
		leaveService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("leave:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		leaveService.logicDel(id);
		return R.ok();
	}
	
}
