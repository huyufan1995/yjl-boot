package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.GroupRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 转发记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:36
 */
public interface GroupRecordDao extends BaseDao<GroupRecordEntity> {

	List<GroupRecordEntity> queryObjectByParam(Map<String, Object> map);
	
}
