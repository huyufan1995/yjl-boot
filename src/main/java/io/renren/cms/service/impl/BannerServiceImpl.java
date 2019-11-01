package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.BannerDao;
import io.renren.cms.entity.BannerEntity;
import io.renren.cms.service.BannerService;



@Service("bannerService")
public class BannerServiceImpl implements BannerService {
	@Autowired
	private BannerDao bannerDao;
	
	@Override
	public BannerEntity queryObject(Integer id){
		return bannerDao.queryObject(id);
	}
	
	@Override
	public List<BannerEntity> queryList(Map<String, Object> map){
		return bannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return bannerDao.queryTotal(map);
	}
	
	@Override
	public void save(BannerEntity banner){
		bannerDao.save(banner);
	}
	
	@Override
	public void update(BannerEntity banner){
		bannerDao.update(banner);
	}
	
	@Override
	public void delete(Integer id){
		bannerDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		bannerDao.deleteBatch(ids);
	}

	@Override
	public BannerEntity queryObjectByType(String type) {
		return bannerDao.queryObjectByType(type);
	}
	
}
