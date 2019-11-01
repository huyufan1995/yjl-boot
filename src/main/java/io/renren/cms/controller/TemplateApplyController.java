package io.renren.cms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.json.JSONUtil;
import io.renren.api.vo.TemplateApplyEntityVo;
import io.renren.cms.entity.TemplateApplyEntity;
import io.renren.cms.service.TemplateApplyService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 报名模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:45
 */
@RestController
@RequestMapping("templateapply")
public class TemplateApplyController {
	@Autowired
	private TemplateApplyService templateApplyService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templateapply:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<TemplateApplyEntity> templateApplyList = templateApplyService.queryList(query);
		int total = templateApplyService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateApplyList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templateapply:info")
	public R info(@PathVariable("id") Integer id){
		TemplateApplyEntity templateApply = templateApplyService.queryObject(id);
		String layout = templateApply.getLayout();
		TemplateApplyEntityVo templateApplyEntityVo = JSONUtil.toBean(layout, TemplateApplyEntityVo.class);
		BeanUtils.copyProperties(templateApply,templateApplyEntityVo);
		return R.ok().put("templateApply", templateApplyEntityVo);
	}

	/**
	 * 发布活动模版
	 */
	@RequestMapping("/releaseapply/{id}")
	//@RequiresPermissions("templateapply:info")
	public R releaseApply(@PathVariable("id") Integer id){
		TemplateApplyEntity templateApplyEntity = new TemplateApplyEntity();
		templateApplyEntity.setId(id);
		templateApplyEntity.setIsRelease("t");
		templateApplyService.update(templateApplyEntity);
		return R.ok();
	}

	/**
	 * 撤回活动模版
	 */
	@RequestMapping("/revocationapply/{id}")
	//@RequiresPermissions("templateapply:info")
	public R revocationapply(@PathVariable("id") Integer id){
		TemplateApplyEntity templateApplyEntity = new TemplateApplyEntity();
		templateApplyEntity.setId(id);
		templateApplyEntity.setIsRelease("f");
		templateApplyService.update(templateApplyEntity);
		return R.ok();
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templateapply:save")
	public R save(@RequestBody TemplateApplyEntityVo templateApplyEntityVo){
		TemplateApplyEntityVo vo = new TemplateApplyEntityVo();
		vo.setBg(templateApplyEntityVo.getBg());
		vo.setBtn(templateApplyEntityVo.getBtn());
		String layout = JSONUtil.toJsonStr(vo);
		TemplateApplyEntity templateApplyEntity = new TemplateApplyEntity();
		templateApplyEntity.setExamplePath(templateApplyEntityVo.getExamplePath());
		templateApplyEntity.setCtime(new Date());
		templateApplyEntity.setUtime(new Date());
		templateApplyEntity.setIsDel("f");
		templateApplyEntity.setIsRelease("f");
		templateApplyEntity.setLayout(layout);
		templateApplyEntity.setInitUseCnt(0);
		templateApplyEntity.setUseCnt(0);
		templateApplyService.save(templateApplyEntity);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templateapply:update")
	public R update(@RequestBody TemplateApplyEntityVo templateApplyEntityVo){
		TemplateApplyEntityVo vo = new TemplateApplyEntityVo();
		vo.setBg(templateApplyEntityVo.getBg());
		vo.setBtn(templateApplyEntityVo.getBtn());
		String layout = JSONUtil.toJsonStr(vo);
		TemplateApplyEntity templateApplyEntity = new TemplateApplyEntity();
		templateApplyEntity.setExamplePath(templateApplyEntityVo.getExamplePath());
		templateApplyEntity.setUtime(new Date());
		templateApplyEntity.setIsDel("f");
		templateApplyEntity.setIsRelease("f");
		templateApplyEntity.setLayout(layout);
		templateApplyEntity.setInitUseCnt(0);
		templateApplyEntity.setUseCnt(0);
		templateApplyEntity.setId(templateApplyEntityVo.getId());
		templateApplyService.update(templateApplyEntity);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templateapply:delete")
	public R delete(@RequestBody Integer[] ids){
		templateApplyService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templateapply:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateApplyService.logicDel(id);
		return R.ok();
	}
	
}
