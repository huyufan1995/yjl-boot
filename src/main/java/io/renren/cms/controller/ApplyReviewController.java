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

import io.renren.cms.entity.ApplyReviewEntity;
import io.renren.cms.service.ApplyReviewService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 活动回顾
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 17:31:14
 */
@RestController
@RequestMapping("applyreview")
public class ApplyReviewController {
	@Autowired
	private ApplyReviewService applyReviewService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("applyreview:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<ApplyReviewEntity> applyReviewList = applyReviewService.queryList(query);
		int total = applyReviewService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyReviewList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("applyreview:info")
	public R info(@PathVariable("id") Integer id){
		ApplyReviewEntity applyReview = applyReviewService.queryObject(id);
		
		return R.ok().put("applyReview", applyReview);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("applyreview:save")
	public R save(@RequestBody ApplyReviewEntity applyReview){
		applyReview.setCtime(new Date());
		applyReview.setIsDel(SystemConstant.F_STR);
		applyReview.setAuditStatus(AuditStatusEnum.UNCOMMIT.getCode());

		if (StringUtils.isNotEmpty(applyReview.getVideoLink())) {
			applyReview.setApplyReviewType(SystemConstant.VIDEO_TYPE);
		} else if (applyReview.getApplyReviewContent().indexOf("<img") > 0) {
			applyReview.setApplyReviewType(SystemConstant.IMAGE_TYPE);
		} else {
			applyReview.setApplyReviewType(SystemConstant.TEXT_TYPE);
		}
		applyReviewService.save(applyReview);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("applyreview:update")
	public R update(@RequestBody ApplyReviewEntity applyReview){
		applyReviewService.update(applyReview);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("applyreview:delete")
	public R delete(@RequestBody Integer[] ids){
		applyReviewService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("applyreview:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyReviewService.logicDel(id);
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
		List<ApplyReviewEntity> applyList = applyReviewService.queryList(query);
		int total = applyReviewService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(applyList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
	/**
	 * 审核通过
	 */
	@RequestMapping("/release/{id}")
	//@RequiresPermissions("information:logicDel")
	public R release(@PathVariable("id") Integer id) {
		int release = applyReviewService.release(id);
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
		int release = applyReviewService.commit(id);
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
	public R commit(@RequestBody ApplyReviewEntity applyReviewEntity) {
		applyReviewEntity.setAuditStatus(AuditStatusEnum.REJECT.getCode());
		applyReviewService.update(applyReviewEntity);
		return R.ok();
	}

	/**
	 * 活动撤回
	 */
	@RequestMapping("/revocation/{id}")
	//@RequiresPermissions("information:logicDel")
	public R revocation(@PathVariable("id") Integer id) {
		int revocation = applyReviewService.revocation(id);
		if(revocation>0){
			return R.ok();
		}else{
			return R.error();
		}
	}

}
