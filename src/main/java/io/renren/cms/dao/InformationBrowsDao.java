package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.InformationBrowsEntity;
import io.renren.dao.BaseDao;

/**
 * 资讯浏览记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 14:39:16
 */
public interface InformationBrowsDao extends BaseDao<InformationBrowsEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	List<String> queryPortrait(String informationId);
}
