package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseShareEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.CaseShareDao;
import io.renren.cms.entity.CaseShareEntity;
import io.renren.cms.service.CaseShareService;

/**
 * 案例-分享服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("caseShareService")
public class CaseShareServiceImpl implements CaseShareService {

	@Autowired
	private CaseShareDao caseShareDao;

	@Override
	public CaseShareEntity queryObject(Integer id) {
		return caseShareDao.queryObject(id);
	}

	@Override
	public List<CaseShareEntity> queryList(Map<String, Object> map) {
		return caseShareDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return caseShareDao.queryTotal(map);
	}

	@Override
	public void save(CaseShareEntity caseShare) {
		caseShareDao.save(caseShare);
	}

	@Override
	public void update(CaseShareEntity caseShare) {
		caseShareDao.update(caseShare);
	}

	@Override
	public void delete(Integer id) {
		caseShareDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		caseShareDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return caseShareDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return caseShareDao.logicDelBatch(ids);
	}

	@Override
	public CaseShareEntity queryObjectByCaseIdAndShareMemberId(Integer caseId, Integer shareMemberId) {
		return caseShareDao.queryObjectByCaseIdAndShareMemberId(caseId, shareMemberId);
	}

	@Override
	public List<CaseShareEntityVo> queryListVo(Map<String, Object> map) {
		return caseShareDao.queryListVo(map);
	}

}
