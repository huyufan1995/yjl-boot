package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.LikeEntity;
import io.renren.dao.BaseDao;

/**
 * 点赞表
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
public interface LikeDao extends BaseDao<LikeEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
