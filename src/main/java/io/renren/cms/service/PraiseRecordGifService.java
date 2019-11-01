package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.PraiseRecordGifEntity;

/**
 * 动图点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
public interface PraiseRecordGifService {

	PraiseRecordGifEntity queryObject(Integer id);

	List<PraiseRecordGifEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(PraiseRecordGifEntity praiseRecordGif);

	void update(PraiseRecordGifEntity praiseRecordGif);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	PraiseRecordGifEntity queryObjectByTemplateIdAndOpenId(String openId, Integer templateId);
}
