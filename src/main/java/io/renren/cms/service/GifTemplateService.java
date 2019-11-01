package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.GifTemplateEntity;
import io.renren.cms.entity.UseRecordEntity;

/**
 * 动图模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
public interface GifTemplateService {

	GifTemplateEntity queryObject(Integer id);

	List<GifTemplateEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(GifTemplateEntity gifTemplate);

	void update(GifTemplateEntity gifTemplate);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	UseRecordEntity generate(Integer id, String sentences, String openid) throws Exception;
}
