package io.renren.cms.service;

import io.renren.cms.entity.ApplyVerifyEntity;

import java.util.List;
import java.util.Map;

/**
 * 活动核销人员配置接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:36
 */
public interface ApplyVerifyService {
	
	ApplyVerifyEntity queryObject(Integer id);
	
	List<ApplyVerifyEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ApplyVerifyEntity applyVerify);
	
	void update(ApplyVerifyEntity applyVerify);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
