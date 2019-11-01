package io.renren.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.TemplateApplyDao;
import io.renren.cms.entity.TemplateApplyEntity;
import io.renren.cms.service.TemplateApplyService;

/**
 * 报名模板服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateApplyService")
public class TemplateApplyServiceImpl implements TemplateApplyService {

	@Autowired
	private TemplateApplyDao templateApplyDao;
	
	@Override
	public TemplateApplyEntity queryObject(Integer id){
		return templateApplyDao.queryObject(id);
	}
	
	@Override
	public List<TemplateApplyEntity> queryList(Map<String, Object> map){
		return templateApplyDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return templateApplyDao.queryTotal(map);
	}
	
	@Override
	public void save(TemplateApplyEntity templateApply){
		templateApplyDao.save(templateApply);
	}
	
	@Override
	public void update(TemplateApplyEntity templateApply){
		templateApplyDao.update(templateApply);
	}
	
	@Override
	public void delete(Integer id){
		templateApplyDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		templateApplyDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return templateApplyDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateApplyDao.logicDelBatch(ids);
	}
	
	
}
