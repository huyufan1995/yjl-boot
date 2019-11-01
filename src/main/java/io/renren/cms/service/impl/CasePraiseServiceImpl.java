package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CasePraiseEntityVo;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.CasePraiseDao;
import io.renren.cms.entity.CasePraiseEntity;
import io.renren.cms.service.CasePraiseService;

/**
 * 案例-赞服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("casePraiseService")
public class CasePraiseServiceImpl implements CasePraiseService {

	@Autowired
	private CasePraiseDao casePraiseDao;

	@Override
	public CasePraiseEntity queryObject(Integer id) {
		return casePraiseDao.queryObject(id);
	}

	@Override
	public List<CasePraiseEntity> queryList(Map<String, Object> map) {
		return casePraiseDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return casePraiseDao.queryTotal(map);
	}

	@Override
	public void save(CasePraiseEntity casePraise) {
		casePraiseDao.save(casePraise);
	}

	@Override
	public void update(CasePraiseEntity casePraise) {
		casePraiseDao.update(casePraise);
	}

	@Override
	public void delete(Integer id) {
		casePraiseDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		casePraiseDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return casePraiseDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return casePraiseDao.logicDelBatch(ids);
	}

	@Override
	public CasePraiseEntity queryObjectByOpenidAndCaseId(String openid, Integer caseId) {
		return casePraiseDao.queryObjectByOpenidAndCaseId(openid, caseId);
	}

	@Override
	public List<CasePraiseEntityVo> queryListVo(Map<String, Object> map) {
		return casePraiseDao.queryListVo(map);
	}

}
