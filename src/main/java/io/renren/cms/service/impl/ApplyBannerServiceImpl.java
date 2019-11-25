package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.ApplyBannerDao;
import io.renren.cms.entity.ApplyBannerEntity;
import io.renren.cms.service.ApplyBannerService;

/**
 * 服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("applyBannerService")
public class ApplyBannerServiceImpl implements ApplyBannerService {

	@Autowired
	private ApplyBannerDao applyBannerDao;
	
	@Override
	public ApplyBannerEntity queryObject(Integer id){
		return applyBannerDao.queryObject(id);
	}
	
	@Override
	public List<ApplyBannerEntity> queryList(Map<String, Object> map){
		return applyBannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return applyBannerDao.queryTotal(map);
	}
	
	@Override
	public void save(ApplyBannerEntity applyBanner){
		applyBannerDao.save(applyBanner);
	}
	
	@Override
	public void update(ApplyBannerEntity applyBanner){
		applyBannerDao.update(applyBanner);
	}
	
	@Override
	public void delete(Integer id){
		applyBannerDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		applyBannerDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return applyBannerDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return applyBannerDao.logicDelBatch(ids);
	}
	
	
}
