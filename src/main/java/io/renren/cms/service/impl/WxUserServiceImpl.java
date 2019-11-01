package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.WxUserDao;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.WxUserService;



@Service("wxUserService")
public class WxUserServiceImpl implements WxUserService {
	@Autowired
	private WxUserDao wxUserDao;
	
	@Override
	public WxUserEntity queryObject(Long id){
		return wxUserDao.queryObject(id);
	}
	
	@Override
	public List<WxUserEntity> queryList(Map<String, Object> map){
		return wxUserDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return wxUserDao.queryTotal(map);
	}
	
	@Override
	public void save(WxUserEntity wxUser){
		wxUserDao.save(wxUser);
	}
	
	@Override
	public void update(WxUserEntity wxUser){
		wxUserDao.update(wxUser);
	}
	
	@Override
	public void delete(Long id){
		wxUserDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		wxUserDao.deleteBatch(ids);
	}

	@Override
	public WxUserEntity queryByOpenId(String openid) {
		return wxUserDao.queryByOpenId(openid);
	}
	
}
