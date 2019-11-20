package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.MemberBannerEntity;
import io.renren.dao.BaseDao;

/**
 * 会员banner
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 11:15:13
 */
public interface MemberBannerDao extends BaseDao<MemberBannerEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
