package io.renren.dao;

import io.renren.entity.TokenEntity;

/**
 * 用户Token
 * 
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface TokenDao extends BaseDao<TokenEntity> {

	TokenEntity queryByUserId(Integer userId);

	TokenEntity queryByToken(String token);

}
