package io.renren.cms.dao;

import java.util.List;

import io.renren.cms.entity.CertificateEntity;
import io.renren.dao.BaseDao;

/**
 * 认证
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface CertificateDao extends BaseDao<CertificateEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);
	
	CertificateEntity queryObjectByMemberId(Integer memberId);
}
