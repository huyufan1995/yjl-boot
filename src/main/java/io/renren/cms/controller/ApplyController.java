package io.renren.cms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.ApplyEntity;
import io.renren.enums.AuditStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.service.ApplyService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 活动
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 19:02:39
 */
@RestController
@RequestMapping("apply")
public class ApplyController {
	@Autowired
	private ApplyService applyService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("apply:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<ApplyEntity> applyList = applyService.queryList(query);
		int total = applyService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("apply:info")
	public R info(@PathVariable("id") Integer id){
		ApplyEntity apply = applyService.queryObject(id);
		
		return R.ok().put("apply", apply);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("apply:save")
	public R save(@RequestBody ApplyEntity apply){
		checkApply(apply);
		applyService.save(apply);
		return R.ok();
	}

	private void checkApply(@RequestBody ApplyEntity apply) {
		apply.setCtime(new Date());
		apply.setApplyHot((int)(Math.random()*900 + 100));
		apply.setCreatePeople(SystemConstant.CREATE_PEOPLE);
		apply.setIsDel(SystemConstant.F_STR);
		apply.setAuditStatus(AuditStatusEnum.UNCOMMIT.getCode());
		if (StringUtils.isNotEmpty(apply.getVideoLink())) {
			if(StringUtils.isEmpty(apply.getBanner())){
				apply.setBanner(SystemConstant.DEFAULT_VEDIO_IMG);
			}
			apply.setApplyContentType(SystemConstant.VIDEO_TYPE);
		} else if (apply.getApplyContent().indexOf("<img") > 0) {
			apply.setApplyContentType(SystemConstant.IMAGE_TYPE);
		} else {
			apply.setApplyContentType(SystemConstant.TEXT_TYPE);
		}
		if(StringUtils.isEmpty(apply.getBanner())){
			apply.setBanner(SystemConstant.DEFAULT_TEXT_IMG);
		}
	}

	@RequestMapping("/update")
	//@RequiresPermissions("apply:update")
	public R update(@RequestBody ApplyEntity apply){
		applyService.update(apply);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("apply:delete")
	public R delete(@RequestBody Integer[] ids){
		applyService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("apply:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyService.logicDel(id);
		return R.ok();
	}

	/**
	 * 审核列表
	 */
	@RequestMapping("/adminList")
	//@RequiresPermissions("apply:list")
	public R adminList(@RequestParam Map<String, Object> params){
		params.put("auditStatus","pending");
		Query query = new Query(params);
		List<ApplyEntity> applyList = applyService.queryList(query);
		int total = applyService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(applyList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 审核通过
	 */
	@RequestMapping("/release/{id}")
	//@RequiresPermissions("information:logicDel")
	public R release(@PathVariable("id") Integer id) {
		int release = applyService.release(id);
		if(release>0){
			return R.ok();
		}else{
			return R.error();
		}
	}

	/**
	 * 提交审核
	 */
	@RequestMapping("/commit/{id}")
	//@RequiresPermissions("information:logicDel")
	public R commit(@PathVariable("id") Integer id) {
		int release = applyService.commit(id);
		if(release>0){
			return R.ok();
		}else{
			return R.error();
		}
	}

	/**
	 * 审核不通过
	 */
	@RequestMapping("/reject")
	//@RequiresPermissions("information:logicDel")
	public R commit(@RequestBody ApplyEntity applyEntity) {
		applyEntity.setAuditStatus(AuditStatusEnum.REJECT.getCode());
		applyService.update(applyEntity);
		return R.ok();
	}

	/**
	 * 活动撤回
	 */
	@RequestMapping("/revocation/{id}")
	//@RequiresPermissions("information:logicDel")
	public R revocation(@PathVariable("id") Integer id) {
		int revocation = applyService.revocation(id);
		if(revocation>0){
			return R.ok();
		}else{
			return R.error();
		}
	}

	/**
	 * 列表
	 */
	@RequestMapping("/queryAll")
	//@RequiresPermissions("apply:list")
	public R queryAll(){
		List<ApplyEntity> applyList = applyService.queryAll();
		return R.ok().put("applyList", applyList);
	}
}
