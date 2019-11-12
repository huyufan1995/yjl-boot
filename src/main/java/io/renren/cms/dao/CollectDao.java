package io.renren.cms.dao;

import java.util.HashMap;
import java.util.List;

import io.renren.cms.entity.CollectEntity;
import io.renren.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 17:25:18
 */
public interface CollectDao extends BaseDao<CollectEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	Boolean isCollect(@Param(value = "id") Integer id,@Param(value = "collectType") Integer collectType, @Param(value = "openid")String openid);

    Boolean deleteWithOpenIdAndCollectTypeAndDataId(HashMap<String, Object> params);
}
