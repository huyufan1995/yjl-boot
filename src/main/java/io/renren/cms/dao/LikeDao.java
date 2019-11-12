package io.renren.cms.dao;

import java.util.HashMap;
import java.util.List;

import io.renren.cms.entity.LikeEntity;
import io.renren.dao.BaseDao;

/**
 * 点赞表
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 10:03:39
 */
public interface LikeDao extends BaseDao<LikeEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    Boolean deleteByOpenIdAndLikeTypeAndDataId(HashMap<String, Object> params);
}
