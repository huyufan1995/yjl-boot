package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.CaseBrowseDao;
import io.renren.cms.entity.CaseBrowseEntity;
import io.renren.cms.service.CaseBrowseService;

/**
 * 案例库浏览记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("caseBrowseService")
public class CaseBrowseServiceImpl implements CaseBrowseService {

	@Autowired
	private CaseBrowseDao caseBrowseDao;

	@Override
	public CaseBrowseEntity queryObject(Integer id) {
		return caseBrowseDao.queryObject(id);
	}

	@Override
	public List<CaseBrowseEntity> queryList(Map<String, Object> map) {
		return caseBrowseDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return caseBrowseDao.queryTotal(map);
	}

	@Override
	public void save(CaseBrowseEntity caseBrowse) {
		caseBrowseDao.save(caseBrowse);
	}

	@Override
	public void update(CaseBrowseEntity caseBrowse) {
		caseBrowseDao.update(caseBrowse);
	}

	@Override
	public void delete(Integer id) {
		caseBrowseDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		caseBrowseDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return caseBrowseDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return caseBrowseDao.logicDelBatch(ids);
	}

	@Override
	public CaseBrowseEntity queryObjectByCaseIdAndBrowseOpenidAndShareMemberId(Integer caseId, String browseOpenid,
			Integer shareMemberId) {
		return caseBrowseDao.queryObjectByCaseIdAndBrowseOpenidAndShareMemberId(caseId, browseOpenid, shareMemberId);
	}

	@Override
	public List<CaseBrowseEntity> queryListVo(Map<String, Object> map) {
		return caseBrowseDao.queryListVo(map);
	}

}
