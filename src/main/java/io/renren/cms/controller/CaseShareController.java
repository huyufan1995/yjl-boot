package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseShareEntityVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CaseShareEntity;
import io.renren.cms.service.CaseShareService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 案例-分享
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@RestController
@RequestMapping("caseshare")
public class CaseShareController {
	@Autowired
	private CaseShareService caseShareService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("caseshare:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CaseShareEntityVo> caseShareList = caseShareService.queryListVo(query);
		int total = caseShareService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(caseShareList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("caseshare:info")
	public R info(@PathVariable("id") Integer id){
		CaseShareEntity caseShare = caseShareService.queryObject(id);
		
		return R.ok().put("caseShare", caseShare);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("caseshare:save")
	public R save(@RequestBody CaseShareEntity caseShare){
		caseShareService.save(caseShare);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("caseshare:update")
	public R update(@RequestBody CaseShareEntity caseShare){
		caseShareService.update(caseShare);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("caseshare:delete")
	public R delete(@RequestBody Integer[] ids){
		caseShareService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("caseshare:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		caseShareService.logicDel(id);
		return R.ok();
	}
	
}
