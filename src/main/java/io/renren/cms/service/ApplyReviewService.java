package io.renren.cms.service;

import io.renren.api.dto.ApplyReviewEntityDto;
import io.renren.cms.entity.ApplyReviewEntity;

import java.util.List;
import java.util.Map;

/**
 * 活动回顾接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 17:31:14
 */
public interface ApplyReviewService {
	
	ApplyReviewEntity queryObject(Integer id);
	
	List<ApplyReviewEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ApplyReviewEntity applyReview);
	
	void update(ApplyReviewEntity applyReview);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

	int revocation(Integer id);

	int commit(Integer id);

	int release(Integer id);

    ApplyReviewEntityDto queryObjectDto(Map<String, Object> map);
}
