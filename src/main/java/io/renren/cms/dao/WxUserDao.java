package io.renren.cms.dao;

import io.renren.cms.entity.WxUserEntity;
import io.renren.dao.BaseDao;

/**
 * 微信用户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
public interface WxUserDao extends BaseDao<WxUserEntity> {
	
	WxUserEntity queryByOpenId(String openId);
}
