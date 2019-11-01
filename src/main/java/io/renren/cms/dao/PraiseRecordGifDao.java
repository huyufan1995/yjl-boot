package io.renren.cms.dao;

import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.PraiseRecordGifEntity;
import io.renren.dao.BaseDao;

/**
 * 动图点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
public interface PraiseRecordGifDao extends BaseDao<PraiseRecordGifEntity> {

	PraiseRecordGifEntity queryObjectByTemplateIdAndOpenId(@Param("openId") String openId,
			@Param("templateId") Integer templateId);

}
