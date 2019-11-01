package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.PraiseRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-29 16:03:00
 */
public interface PraiseRecordDao extends BaseDao<PraiseRecordEntity> {

	List<PraiseRecordEntity> queryPraiseList(Map<String, Object> map);

	void changePraiseStatus(PraiseRecordEntity praiseRecord);

	PraiseRecordEntity queryObjectByMap(Map<String, Object> params);
	
}
