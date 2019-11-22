package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.service.MemberService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ApplyVerifyEntity;
import io.renren.cms.service.ApplyVerifyService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 活动核销人员配置
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:36
 */
@RestController
@RequestMapping("applyverify")
public class ApplyVerifyController {
	@Autowired
	private ApplyVerifyService applyVerifyService;

	@Autowired
	private MemberService memberService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("applyverify:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<ApplyVerifyEntity> applyVerifyList = applyVerifyService.queryList(query);
		int total = applyVerifyService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyVerifyList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("applyverify:info")
	public R info(@PathVariable("id") Integer id){
		ApplyVerifyEntity applyVerify = applyVerifyService.queryObject(id);
		
		return R.ok().put("applyVerify", applyVerify);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("applyverify:save")
	@Transactional
	public R save(@RequestBody ApplyVerifyEntity applyVerify){
		applyVerifyService.save(applyVerify);
		memberService.updateVerify(SystemConstant.T_STR,applyVerify.getOpenid());
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("applyverify:update")
	public R update(@RequestBody ApplyVerifyEntity applyVerify){
		applyVerifyService.update(applyVerify);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("applyverify:delete")
	public R delete(@RequestBody Integer[] ids){
		applyVerifyService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("applyverify:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyVerifyService.logicDel(id);
		return R.ok();
	}
	
}
