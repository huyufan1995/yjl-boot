package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptMemberEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.DeptMemberEntity;
import io.renren.cms.service.DeptMemberService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 部门-会员-关系
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
@RestController
@RequestMapping("deptmember")
public class DeptMemberController {
	@Autowired
	private DeptMemberService deptMemberService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("deptmember:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<DeptMemberEntityVo> deptMemberList = deptMemberService.queryListVo(query);
		int total = deptMemberService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(deptMemberList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("deptmember:info")
	public R info(@PathVariable("id") Integer id){
		DeptMemberEntity deptMember = deptMemberService.queryObject(id);
		
		return R.ok().put("deptMember", deptMember);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("deptmember:save")
	public R save(@RequestBody DeptMemberEntity deptMember){
		deptMemberService.save(deptMember);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("deptmember:update")
	public R update(@RequestBody DeptMemberEntity deptMember){
		deptMemberService.update(deptMember);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("deptmember:delete")
	public R delete(@RequestBody Integer[] ids){
		deptMemberService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("deptmember:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		deptMemberService.logicDel(id);
		return R.ok();
	}
	
}
