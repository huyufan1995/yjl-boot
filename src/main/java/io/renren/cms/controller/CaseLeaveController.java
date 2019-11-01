package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseLeaveEntityVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CaseLeaveEntity;
import io.renren.cms.service.CaseLeaveService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 留言-案例库
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
@RestController
@RequestMapping("caseleave")
public class CaseLeaveController {
	@Autowired
	private CaseLeaveService caseLeaveService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("caseleave:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CaseLeaveEntity> caseLeaveList = caseLeaveService.queryList(query);
		int total = caseLeaveService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(caseLeaveList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("caseleave:info")
	public R info(@PathVariable("id") Integer id){
		CaseLeaveEntity caseLeave = caseLeaveService.queryObject(id);
		
		return R.ok().put("caseLeave", caseLeave);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("caseleave:save")
	public R save(@RequestBody CaseLeaveEntity caseLeave){
		caseLeaveService.save(caseLeave);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("caseleave:update")
	public R update(@RequestBody CaseLeaveEntity caseLeave){
		caseLeaveService.update(caseLeave);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("caseleave:delete")
	public R delete(@RequestBody Integer[] ids){
		caseLeaveService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("caseleave:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		caseLeaveService.logicDel(id);
		return R.ok();
	}
	
}
