package io.renren.cms.dao;

import io.renren.cms.entity.UseRecordEntity;
import io.renren.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * 使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface UseRecordDao extends BaseDao<UseRecordEntity> {

	List<UseRecordEntity> queryListByTemplateId(Integer templateId);

	List<UseRecordEntity> queryListByTemplateIdGroup(Integer templateId);

	List<UseRecordEntity> queryListByGroup(Map<String, Object> map);

}
