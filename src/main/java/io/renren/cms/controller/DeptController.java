package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.DeptEntity;
import io.renren.cms.service.DeptService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 部门
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
@RestController
@RequestMapping("dept")
public class DeptController {
	@Autowired
	private DeptService deptService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("dept:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<DeptEntityVo> deptList = deptService.queryListVo(query);
		int total = deptService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(deptList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("dept:info")
	public R info(@PathVariable("id") Integer id){
		DeptEntity dept = deptService.queryObject(id);
		
		return R.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("dept:save")
	public R save(@RequestBody DeptEntity dept){
		deptService.save(dept);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("dept:update")
	public R update(@RequestBody DeptEntity dept){
		deptService.update(dept);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("dept:delete")
	public R delete(@RequestBody Integer[] ids){
		deptService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("dept:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		deptService.logicDel(id);
		return R.ok();
	}
	
}
