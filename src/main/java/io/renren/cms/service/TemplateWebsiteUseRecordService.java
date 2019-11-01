package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.TemplateWebsiteUseRecordEntityVo;
import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import io.renren.utils.Query;

/**
 * 官网模板使用记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
public interface TemplateWebsiteUseRecordService {

	TemplateWebsiteUseRecordEntity queryObject(Integer id);

	List<TemplateWebsiteUseRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(TemplateWebsiteUseRecordEntity templateWebsiteUseRecord);

	void update(TemplateWebsiteUseRecordEntity templateWebsiteUseRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	TemplateWebsiteUseRecordEntity queryObjectByTemplateWebsiteIdAndMemberId(Integer templateWebsiteId,
			Integer memberId);

	List<TemplateWebsiteUseRecordEntity> queryListByTemplateWebsiteId(Integer templateWebsiteId);

    List<TemplateWebsiteUseRecordEntityVo> queryListVo(Map<String, Object> map);
}
