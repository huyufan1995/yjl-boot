package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.InformationTypeEntity;
import io.renren.dao.BaseDao;

/**
 * 
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-07 17:22:16
 */
public interface InformationTypeDao extends BaseDao<InformationTypeEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
