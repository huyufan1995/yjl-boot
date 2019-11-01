package io.renren.cms.service;

import io.renren.cms.entity.ArticleEntity;

import java.util.List;
import java.util.Map;

/**
 * 文章
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-24 12:27:52
 */
public interface ArticleService {
	
	ArticleEntity queryObject(Integer id);
	
	List<ArticleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ArticleEntity article);
	
	void update(ArticleEntity article);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
