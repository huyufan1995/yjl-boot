package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.service.UseRecordService;
import io.renren.properties.YykjProperties;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@RestController
@RequestMapping("userecord")
public class UseRecordController {
	@Autowired
	private UseRecordService useRecordService;
	
	@Autowired
	private YykjProperties yykjProperties;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<UseRecordEntity> useRecordList = useRecordService.queryList(query);
		useRecordList.forEach(u -> {
			u.setTemplateImageExample(yykjProperties.getVisitprefix().concat(u.getTemplateImageExample()));
			u.setTemplateImageResult(yykjProperties.getVisitprefix().concat(u.getTemplateImageResult()));
		});
		int total = useRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(useRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		UseRecordEntity useRecord = useRecordService.queryObject(id);
		
		return R.ok().put("useRecord", useRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody UseRecordEntity useRecord){
		useRecordService.save(useRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody UseRecordEntity useRecord){
		useRecordService.update(useRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		useRecordService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
