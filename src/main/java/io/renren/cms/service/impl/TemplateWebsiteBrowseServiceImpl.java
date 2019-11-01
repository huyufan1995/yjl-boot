package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteBrowseEntityVo;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.TemplateWebsiteBrowseDao;
import io.renren.cms.entity.TemplateWebsiteBrowseEntity;
import io.renren.cms.service.TemplateWebsiteBrowseService;

/**
 * 官网模板浏览记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateWebsiteBrowseService")
public class TemplateWebsiteBrowseServiceImpl implements TemplateWebsiteBrowseService {

	@Autowired
	private TemplateWebsiteBrowseDao templateWebsiteBrowseDao;

	@Override
	public TemplateWebsiteBrowseEntity queryObject(Integer id) {
		return templateWebsiteBrowseDao.queryObject(id);
	}

	@Override
	public List<TemplateWebsiteBrowseEntity> queryList(Map<String, Object> map) {
		return templateWebsiteBrowseDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateWebsiteBrowseDao.queryTotal(map);
	}

	@Override
	public void save(TemplateWebsiteBrowseEntity templateWebsiteBrowse) {
		templateWebsiteBrowseDao.save(templateWebsiteBrowse);
	}

	@Override
	public void update(TemplateWebsiteBrowseEntity templateWebsiteBrowse) {
		templateWebsiteBrowseDao.update(templateWebsiteBrowse);
	}

	@Override
	public void delete(Integer id) {
		templateWebsiteBrowseDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateWebsiteBrowseDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return templateWebsiteBrowseDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateWebsiteBrowseDao.logicDelBatch(ids);
	}

	@Override
	public TemplateWebsiteBrowseEntity queryObjectByOwnerOpenidAndBrowseOpenid(String ownerOpenid,
			String browseOpenid) {
		return templateWebsiteBrowseDao.queryObjectByOwnerOpenidAndBrowseOpenid(ownerOpenid, browseOpenid);
	}

	@Override
	public List<TemplateWebsiteBrowseEntity> queryListByOwnerOpenid(String ownerOpenid) {
		return templateWebsiteBrowseDao.queryListByOwnerOpenid(ownerOpenid);
	}

	@Override
	public List<TemplateWebsiteBrowseEntityVo> queryListVo(Map<String, Object> map) {
		return templateWebsiteBrowseDao.queryListVo(map);
	}

}
