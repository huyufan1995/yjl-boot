package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.service.CommentService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 评论表
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
@RestController
@RequestMapping("comment")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("comment:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<CommentEntity> commentList = commentService.queryList(query);
		int total = commentService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(commentList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("comment:info")
	public R info(@PathVariable("id") Integer id){
		CommentEntity comment = commentService.queryObject(id);
		
		return R.ok().put("comment", comment);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("comment:save")
	public R save(@RequestBody CommentEntity comment){
		commentService.save(comment);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("comment:update")
	public R update(@RequestBody CommentEntity comment){
		commentService.update(comment);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("comment:delete")
	public R delete(@RequestBody Integer[] ids){
		commentService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("comment:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		commentService.logicDel(id);
		return R.ok();
	}
	
}
