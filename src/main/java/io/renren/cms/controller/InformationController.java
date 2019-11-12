package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.InformationsEntity;
import io.renren.enums.AuditStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.service.InformationService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author moran
 * @date 2019-11-05 10:36:31
 */
@RestController
@RequestMapping("information")
public class InformationController {
	@Autowired
	private InformationService informationService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("information:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<InformationsEntity> informationList = informationService.queryList(query);
		int total = informationService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(informationList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * admin管理资讯列表
	 */
	@RequestMapping("/adminList")
	//@RequiresPermissions("information:list")
	public R adminList(@RequestParam Map<String, Object> params){
		params.put("auditStatus","pending");
		Query query = new Query(params);
		List<InformationsEntity> informationList = informationService.queryList(query);
		int total = informationService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(informationList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("information:info")
	public R info(@PathVariable("id") Integer id){
		InformationsEntity information = informationService.queryObject(id);
		
		return R.ok().put("information", information);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("information:save")
	public R save(@RequestBody InformationsEntity information){
		information = checkInformationType(information);
		informationService.save(information);
		return R.ok();
	}

	/**
	 * 判断资讯类型 1：纯文字 2：图文 3：视频
	 * @param information
	 * @return
	 */
	private InformationsEntity checkInformationType(InformationsEntity information) {
		if (StringUtils.isNotEmpty(information.getVideoLink())) {
			information.setContentType(SystemConstant.VIDEO_TYPE);
		} else if (information.getContent().indexOf("<img") > 0) {
			information.setContentType(SystemConstant.IMAGE_TYPE);
		} else {
			information.setContentType(SystemConstant.TEXT_TYPE);
		}
		return information;
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("information:update")
	public R update(@RequestBody InformationsEntity information){
		information = checkInformationType(information);
		informationService.update(information);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("information:delete")
	public R delete(@RequestBody Integer[] ids){
		informationService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("information:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		informationService.logicDel(id);
		return R.ok();
	}

    /**
     * 审核通过
     */
    @RequestMapping("/release/{id}")
    //@RequiresPermissions("information:logicDel")
    public R release(@PathVariable("id") Integer id) {
        int release = informationService.release(id);
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
		int release = informationService.commit(id);
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
	public R commit(@RequestBody InformationsEntity informationsEntity) {
		informationsEntity.setAuditStatus(AuditStatusEnum.REJECT.getCode());
		informationService.update(informationsEntity);
		return R.ok();
	}

	/**
	 * 资讯撤回
	 */
	@RequestMapping("/revocation/{id}")
	//@RequiresPermissions("information:logicDel")
	public R revocation(@PathVariable("id") Integer id) {
		int revocation = informationService.revocation(id);
		if(revocation>0){
			return R.ok();
		}else{
			return R.error();
		}
	}
	
}