package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.TemplateWebsiteDao;
import io.renren.cms.entity.TemplateWebsiteEntity;
import io.renren.cms.service.TemplateWebsiteService;

/**
 * 官网模板服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateWebsiteService")
public class TemplateWebsiteServiceImpl implements TemplateWebsiteService {

	@Autowired
	private TemplateWebsiteDao templateWebsiteDao;
	
	@Override
	public TemplateWebsiteEntity queryObject(Integer id){
		return templateWebsiteDao.queryObject(id);
	}

	@Override
	public TemplateWebsiteEntity queryByLayoutWithCtime() {
		return templateWebsiteDao.queryByLayoutWithCtime();
	}

	@Override
	public List<TemplateWebsiteEntity> queryList(Map<String, Object> map){
		return templateWebsiteDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return templateWebsiteDao.queryTotal(map);
	}
	
	@Override
	public void save(TemplateWebsiteEntity templateWebsite){
		templateWebsiteDao.save(templateWebsite);
	}
	
	@Override
	public void update(TemplateWebsiteEntity templateWebsite){
		templateWebsiteDao.update(templateWebsite);
	}
	
	@Override
	public void delete(Integer id){
		templateWebsiteDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		templateWebsiteDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return templateWebsiteDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateWebsiteDao.logicDelBatch(ids);
	}
	
	
}
