package io.renren.cms.service;

import io.renren.cms.entity.MemberBannerEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员banner接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 11:15:13
 */
public interface MemberBannerService {
	
	MemberBannerEntity queryObject(Integer id);
	
	List<MemberBannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MemberBannerEntity memberBanner);
	
	void update(MemberBannerEntity memberBanner);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
