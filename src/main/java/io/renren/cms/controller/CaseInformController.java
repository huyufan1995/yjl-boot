package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseInformEntityVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CaseInformEntity;
import io.renren.cms.service.CaseInformService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 案例-举报
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-10 14:58:35
 */
@RestController
@RequestMapping("caseinform")
public class CaseInformController {
	@Autowired
	private CaseInformService caseInformService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("caseinform:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CaseInformEntityVo> caseInformList = caseInformService.queryListVo(query);
		int total = caseInformService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(caseInformList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("caseinform:info")
	public R info(@PathVariable("id") Integer id){
		CaseInformEntity caseInform = caseInformService.queryObject(id);
		
		return R.ok().put("caseInform", caseInform);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("caseinform:save")
	public R save(@RequestBody CaseInformEntity caseInform){
		caseInformService.save(caseInform);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("caseinform:update")
	public R update(@RequestBody CaseInformEntity caseInform){
		caseInformService.update(caseInform);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("caseinform:delete")
	public R delete(@RequestBody Integer[] ids){
		caseInformService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("caseinform:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		caseInformService.logicDel(id);
		return R.ok();
	}
	
}
