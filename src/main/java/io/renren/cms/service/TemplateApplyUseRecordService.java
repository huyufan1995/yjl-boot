package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.TemplateApplyUseRecordEntity;

/**
 * 报名模板使用记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 17:54:54
 */
public interface TemplateApplyUseRecordService {

	TemplateApplyUseRecordEntity queryObject(Integer id);

	List<TemplateApplyUseRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TemplateApplyUseRecordEntity templateApplyUseRecord);

	void update(TemplateApplyUseRecordEntity templateApplyUseRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<TemplateApplyUseRecordEntity> queryListByTemplateApplyId(Integer templateApplyId);
}
