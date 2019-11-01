package io.renren.service;

import java.util.Map;

import io.renren.entity.TokenEntity;

/**
 * 用户Token
 * 
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface TokenService {

	TokenEntity queryByUserId(Integer userId);

	TokenEntity queryByToken(String token);

	void save(TokenEntity token);

	void update(TokenEntity token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 * @return        返回token相关信息
	 */
	Map<String, Object> createToken(Integer userId);

}
