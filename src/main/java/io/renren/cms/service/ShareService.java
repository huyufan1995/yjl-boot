package io.renren.cms.service;

import io.renren.cms.entity.ShareEntity;

import java.util.List;
import java.util.Map;

/**
 * 分享表接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
public interface ShareService {
	
	ShareEntity queryObject(Integer id);
	
	List<ShareEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ShareEntity share);
	
	void update(ShareEntity share);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);
}
