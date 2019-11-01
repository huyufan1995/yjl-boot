package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CasePraiseEntityVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CasePraiseEntity;
import io.renren.cms.service.CasePraiseService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 案例-赞
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@RestController
@RequestMapping("casepraise")
public class CasePraiseController {
	@Autowired
	private CasePraiseService casePraiseService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("casepraise:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CasePraiseEntityVo> casePraiseList = casePraiseService.queryListVo(query);
		int total = casePraiseService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(casePraiseList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("casepraise:info")
	public R info(@PathVariable("id") Integer id){
		CasePraiseEntity casePraise = casePraiseService.queryObject(id);
		
		return R.ok().put("casePraise", casePraise);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("casepraise:save")
	public R save(@RequestBody CasePraiseEntity casePraise){
		casePraiseService.save(casePraise);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("casepraise:update")
	public R update(@RequestBody CasePraiseEntity casePraise){
		casePraiseService.update(casePraise);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("casepraise:delete")
	public R delete(@RequestBody Integer[] ids){
		casePraiseService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("casepraise:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		casePraiseService.logicDel(id);
		return R.ok();
	}
	
}
