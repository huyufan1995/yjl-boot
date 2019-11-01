package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.CertificateDao;
import io.renren.cms.entity.CertificateEntity;
import io.renren.cms.service.CertificateService;

/**
 * 认证服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("certificateService")
public class CertificateServiceImpl implements CertificateService {

	@Autowired
	private CertificateDao certificateDao;

	@Override
	public CertificateEntity queryObject(Integer id) {
		return certificateDao.queryObject(id);
	}

	@Override
	public List<CertificateEntity> queryList(Map<String, Object> map) {
		return certificateDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return certificateDao.queryTotal(map);
	}

	@Override
	public void save(CertificateEntity certificate) {
		certificateDao.save(certificate);
	}

	@Override
	public void update(CertificateEntity certificate) {
		certificateDao.update(certificate);
	}

	@Override
	public void delete(Integer id) {
		certificateDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		certificateDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return certificateDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return certificateDao.logicDelBatch(ids);
	}

	@Override
	public CertificateEntity queryObjectByMemberId(Integer memberId) {
		return certificateDao.queryObjectByMemberId(memberId);
	}

}
