package io.renren.cms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.DaysRecordDao;
import io.renren.cms.dao.UseRuleDao;
import io.renren.cms.dao.UserDaysDao;
import io.renren.cms.dao.WxUserDao;
import io.renren.cms.entity.DaysRecordEntity;
import io.renren.cms.entity.UseRuleEntity;
import io.renren.cms.entity.UserDaysEntity;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.UserDaysService;
import io.renren.utils.DateUtils;
import io.renren.utils.validator.Assert;



@Service("userDaysService")
public class UserDaysServiceImpl implements UserDaysService {
	@Autowired
	private UserDaysDao userDaysDao;
	@Autowired
	private DaysRecordDao daysRecordDao ;
	@Autowired
	private WxUserDao wxUserDao ;
	@Autowired
	private UseRuleDao useRuleDao  ;
	
	@Override
	public UserDaysEntity queryObject(Integer id){
		return userDaysDao.queryObject(id);
	}
	
	@Override
	public List<UserDaysEntity> queryList(Map<String, Object> map){
		return userDaysDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userDaysDao.queryTotal(map);
	}
	
	@Override
	public void save(UserDaysEntity userDays){
		userDaysDao.save(userDays);
	}
	
	@Override
	public void update(UserDaysEntity userDays){
		userDaysDao.update(userDays);
	}
	
	@Override
	public void delete(Integer id){
		userDaysDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		userDaysDao.deleteBatch(ids);
	}

	@Override
	public UserDaysEntity queryObjectByParam(Map<String, Object> map) {
		return userDaysDao.queryObjectByParam(map);
	}

	@Override
	public UserDaysEntity dealUserDays(String openId,String type) {
		Map<String, Object> map = new HashMap<>();
		WxUserEntity user = wxUserDao.queryByOpenId(openId);
		Assert.isNull(user, "获取微信会员信息异常");
		Date date = new Date();
		//获取从注册到现在的天数
		Integer duration = DateUtils.getDistanceDays(user.getCtime(), date).intValue();
		map.put("openId", openId);
		UserDaysEntity userDays = userDaysDao.queryObjectByParam(map);
		if(null==userDays){
			UseRuleEntity rule = useRuleDao.queryObjectByType("registe");
			userDays = new UserDaysEntity();
			userDays.setCreateTime(date);
			userDays.setIfDeleted("f");
			userDays.setOpenId(openId);
			userDays.setTotalDays(duration+rule.getDays());
			userDays.setRegisteTime(user.getCtime());
			userDays.setUpdateTime(date);
			
			userDaysDao.save(userDays);
			
			//保存 增加使用天数记录
			DaysRecordEntity daysRecord = new DaysRecordEntity();
			daysRecord.setCreateTime(date);
			daysRecord.setDays(rule.getDays());
			daysRecord.setIfDeleted("f");
			daysRecord.setOpenId(openId);
			daysRecord.setType(type);
			daysRecord.setUpdateTime(date);
			daysRecordDao.save(daysRecord);
			
			userDays.setAdd_days(rule.getDays());
			userDays.setRemain_days(rule.getDays());
		}else{
			UseRuleEntity rule = useRuleDao.queryObjectByType(type);
			Integer days = userDays.getTotalDays()-duration;
			if(days<0){
				userDays.setTotalDays(duration+rule.getDays());
			}else{
				userDays.setTotalDays(userDays.getTotalDays()+rule.getDays());
			}
			userDaysDao.update(userDays);
			
			//保存 增加使用天数记录
			DaysRecordEntity daysRecord = new DaysRecordEntity();
			daysRecord.setCreateTime(date);
			daysRecord.setDays(rule.getDays());
			daysRecord.setIfDeleted("f");
			daysRecord.setOpenId(openId);
			daysRecord.setType(type);
			daysRecord.setUpdateTime(date);
			daysRecordDao.save(daysRecord);
			
			//获取增加天数
			userDays.setAdd_days(rule.getDays());
			//获取剩余使用的天数
			userDays.setRemain_days(userDays.getTotalDays()-duration);
		}
		
		return userDays;
	}

	@Override
	public void dealOldUserDays(String openId, String type) {
		Map<String, Object> map = new HashMap<>();
		WxUserEntity user = wxUserDao.queryByOpenId(openId);
		Assert.isNull(user, "获取微信会员信息异常");
		map.put("openId", openId);
		UserDaysEntity userDays = userDaysDao.queryObjectByParam(map);
		if(null==userDays){
			Date date = new Date();
			UseRuleEntity rule = useRuleDao.queryObjectByType("registe");
			//获取从注册到现在的天数
			Integer duration = DateUtils.getDistanceDays(user.getCtime(), date).intValue();
			userDays = new UserDaysEntity();
			userDays.setCreateTime(date);
			userDays.setIfDeleted("f");
			userDays.setOpenId(openId);
			userDays.setTotalDays(duration+rule.getDays());
			userDays.setRegisteTime(user.getCtime());
			userDays.setUpdateTime(date);
			
			userDaysDao.save(userDays);
			
			//保存 增加使用天数记录
			DaysRecordEntity daysRecord = new DaysRecordEntity();
			daysRecord.setCreateTime(date);
			daysRecord.setDays(rule.getDays());
			daysRecord.setIfDeleted("f");
			daysRecord.setOpenId(openId);
			daysRecord.setType("old");
			daysRecord.setUpdateTime(date);
			daysRecordDao.save(daysRecord);
			
			userDays.setAdd_days(rule.getDays());
			userDays.setRemain_days(rule.getDays());
		}
		
	}
	
}
