package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.UseRecordEntity;

/**
 * 使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface UseRecordService {

	UseRecordEntity queryObject(Integer id);

	List<UseRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(UseRecordEntity useRecord);

	void update(UseRecordEntity useRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	List<UseRecordEntity> queryListByTemplateId(Integer templateId);

	List<UseRecordEntity> queryListByGroup(Map<String, Object> map);

	List<UseRecordEntity> queryListByTemplateIdGroup(Integer templateId);
}
