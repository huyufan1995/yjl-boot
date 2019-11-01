package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseInformEntityVo;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.CaseInformDao;
import io.renren.cms.entity.CaseInformEntity;
import io.renren.cms.service.CaseInformService;

/**
 * 案例-举报服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("caseInformService")
public class CaseInformServiceImpl implements CaseInformService {

	@Autowired
	private CaseInformDao caseInformDao;

	@Override
	public CaseInformEntity queryObject(Integer id) {
		return caseInformDao.queryObject(id);
	}

	@Override
	public List<CaseInformEntity> queryList(Map<String, Object> map) {
		return caseInformDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return caseInformDao.queryTotal(map);
	}

	@Override
	public void save(CaseInformEntity caseInform) {
		caseInformDao.save(caseInform);
	}

	@Override
	public void update(CaseInformEntity caseInform) {
		caseInformDao.update(caseInform);
	}

	@Override
	public void delete(Integer id) {
		caseInformDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		caseInformDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return caseInformDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return caseInformDao.logicDelBatch(ids);
	}

	@Override
	public CaseInformEntity queryObjectByOpenidAndCaseId(String openid, Integer caseId) {
		return caseInformDao.queryObjectByOpenidAndCaseId(openid, caseId);
	}


	@Override
	public List<CaseInformEntityVo> queryListVo(Map<String, Object> map) {
		return caseInformDao.queryListVo(map);
	}

}
