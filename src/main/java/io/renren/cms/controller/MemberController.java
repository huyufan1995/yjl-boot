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

import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 会员
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-15 14:20:00
 */
@RestController
@RequestMapping("member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("member:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<MemberEntity> memberList = memberService.queryList(query);
		int total = memberService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(memberList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("member:info")
	public R info(@PathVariable("id") Integer id){
		MemberEntity member = memberService.queryObject(id);
		
		return R.ok().put("member", member);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("member:save")
	public R save(@RequestBody MemberEntity member){
		memberService.save(member);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("member:update")
	public R update(@RequestBody MemberEntity member){
		memberService.update(member);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("member:delete")
	public R delete(@RequestBody Integer[] ids){
		memberService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("member:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		memberService.logicDel(id);
		return R.ok();
	}
	
}
