package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.LikeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.service.LikeService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 点赞表
 * 
 * @author moran
 * @date 2019-11-05 11:05:36
 */
@RestController
@RequestMapping("like")
public class LikeController {
	@Autowired
	private LikeService likeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("like:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<LikeEntity> likeList = likeService.queryList(query);
		int total = likeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(likeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("like:info")
	public R info(@PathVariable("id") Integer id){
		LikeEntity like = likeService.queryObject(id);
		
		return R.ok().put("like", like);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("like:save")
	public R save(@RequestBody LikeEntity like){
		likeService.save(like);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("like:update")
	public R update(@RequestBody LikeEntity like){
		likeService.update(like);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("like:delete")
	public R delete(@RequestBody Integer[] ids){
		likeService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("like:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		likeService.logicDel(id);
		return R.ok();
	}
	
}
