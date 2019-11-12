package io.renren.cms.service.impl;

import io.renren.cms.dao.ArticleDao;
import io.renren.cms.entity.ArticleEntity;
import io.renren.cms.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleDao articleDao;
	
	@Override
	public ArticleEntity queryObject(Integer id){
		return articleDao.queryObject(id);
	}
	
	@Override
	public List<ArticleEntity> queryList(Map<String, Object> map){
		return articleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return articleDao.queryTotal(map);
	}
	
	@Override
	public void save(ArticleEntity article){
		articleDao.save(article);
	}
	
	@Override
	public void update(ArticleEntity article){
		articleDao.update(article);
	}
	
	@Override
	public void delete(Integer id){
		articleDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		articleDao.deleteBatch(ids);
	}
	
}
