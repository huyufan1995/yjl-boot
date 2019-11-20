package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.MemberBannerDao;
import io.renren.cms.entity.MemberBannerEntity;
import io.renren.cms.service.MemberBannerService;

/**
 * 会员banner服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("memberBannerService")
public class MemberBannerServiceImpl implements MemberBannerService {

	@Autowired
	private MemberBannerDao memberBannerDao;
	
	@Override
	public MemberBannerEntity queryObject(Integer id){
		return memberBannerDao.queryObject(id);
	}
	
	@Override
	public List<MemberBannerEntity> queryList(Map<String, Object> map){
		return memberBannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return memberBannerDao.queryTotal(map);
	}
	
	@Override
	public void save(MemberBannerEntity memberBanner){
		memberBannerDao.save(memberBanner);
	}
	
	@Override
	public void update(MemberBannerEntity memberBanner){
		memberBannerDao.update(memberBanner);
	}
	
	@Override
	public void delete(Integer id){
		memberBannerDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		memberBannerDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return memberBannerDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return memberBannerDao.logicDelBatch(ids);
	}
	
	
}
