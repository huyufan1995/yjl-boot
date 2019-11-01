package io.renren.cms.service;

import io.renren.cms.entity.PraiseRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-29 16:03:00
 */
public interface PraiseRecordService {
	
	PraiseRecordEntity queryObject(Integer id);
	
	List<PraiseRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(PraiseRecordEntity praiseRecord);
	
	void update(PraiseRecordEntity praiseRecord);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	List<PraiseRecordEntity> queryPraiseList(Map<String, Object> map);

	void changePraiseStatus(PraiseRecordEntity praiseRecord);

	PraiseRecordEntity queryObjectByMap(Map<String, Object> params);

	boolean ifPraise(Map<String, Object> params);
}
