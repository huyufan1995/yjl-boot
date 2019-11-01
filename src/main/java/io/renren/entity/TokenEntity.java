package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户Token
 * 
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2017-03-23 15:22:07
 */
@Getter
@Setter
public class TokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//用户ID
	private Integer userId;
	//token
	private String token;
	//过期时间
	private Date expireTime;
	//更新时间
	private Date updateTime;

}
