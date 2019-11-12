package io.renren.cms.service;

import io.renren.cms.entity.LikeEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 点赞表接口
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 10:03:39
 */
public interface LikeService {
	
	LikeEntity queryObject(Integer id);
	
	List<LikeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LikeEntity like);
	
	void update(LikeEntity like);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    Boolean deleteByOpenIdAndLikeTypeAndDataId(HashMap<String, Object> params);
}
