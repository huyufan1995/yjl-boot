package io.renren.cms.dao;

import io.renren.cms.entity.CategoryEntity;
import io.renren.dao.BaseDao;

import java.util.List;

/**
 * 分类
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface CategoryDao extends BaseDao<CategoryEntity> {

	List<CategoryEntity> all();
	
}
