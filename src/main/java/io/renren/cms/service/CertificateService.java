package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.CertificateEntity;

/**
 * 认证接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface CertificateService {

	CertificateEntity queryObject(Integer id);

	List<CertificateEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CertificateEntity certificate);

	void update(CertificateEntity certificate);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CertificateEntity queryObjectByMemberId(Integer memberId);
}
