package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.ShareEntity;
import io.renren.dao.BaseDao;

/**
 * 分享表
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
public interface ShareDao extends BaseDao<ShareEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
