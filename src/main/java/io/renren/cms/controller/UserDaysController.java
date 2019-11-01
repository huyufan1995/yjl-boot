package io.renren.cms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.DaysRecordEntity;
import io.renren.cms.entity.UserDaysEntity;
import io.renren.cms.service.DaysRecordService;
import io.renren.cms.service.UserDaysService;
import io.renren.utils.DateUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
@RestController
@RequestMapping("userdays")
public class UserDaysController {
	@Autowired
	private UserDaysService userDaysService;
	@Autowired
	private DaysRecordService daysRecordService ;	
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<UserDaysEntity> userDaysList = userDaysService.queryList(query);
		Date d = new Date();
		for(UserDaysEntity u : userDaysList){
			Integer usedDays = DateUtils.getDistanceDays(u.getRegisteTime(), d).intValue();//已经使用天数
			u.setRemain_days(u.getTotalDays()-usedDays);
		}
		int total = userDaysService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userDaysList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Integer id){
		UserDaysEntity userDays = userDaysService.queryObject(id);
		Date d = new Date();
		Integer usedDays = DateUtils.getDistanceDays(userDays.getRegisteTime(), d).intValue();//已经使用天数
		userDays.setRemain_days(userDays.getTotalDays()-usedDays);
		return R.ok().put("userDays", userDays);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody UserDaysEntity userDays){
		Date date = new Date();
		userDays.setTotalDays(userDays.getTotalDays()+(null!=userDays.getAdd_days()?userDays.getAdd_days():0));
		userDaysService.save(userDays);
		
		//保存 增加使用天数记录
		DaysRecordEntity daysRecord = new DaysRecordEntity();
		daysRecord.setCreateTime(date);
		daysRecord.setDays(null!=userDays.getAdd_days()?userDays.getAdd_days():0);
		daysRecord.setIfDeleted("f");
		daysRecord.setOpenId(userDays.getOpenId());
		daysRecord.setType("cms");
		daysRecord.setUpdateTime(date);
		daysRecordService.save(daysRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody UserDaysEntity userDays){
		Date date = new Date();
		userDays.setTotalDays(userDays.getTotalDays()+(null!=userDays.getAdd_days()?userDays.getAdd_days():0));
		userDays.setUpdateTime(date);
		userDaysService.update(userDays);
		
		
		//保存 增加使用天数记录
		DaysRecordEntity daysRecord = new DaysRecordEntity();
		daysRecord.setCreateTime(date);
		daysRecord.setDays(null!=userDays.getAdd_days()?userDays.getAdd_days():0);
		daysRecord.setIfDeleted("f");
		daysRecord.setOpenId(userDays.getOpenId());
		daysRecord.setType("cms");
		daysRecord.setUpdateTime(date);
		daysRecordService.save(daysRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Integer[] ids){
		userDaysService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
