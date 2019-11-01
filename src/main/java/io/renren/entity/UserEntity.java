package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 * 
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2017-03-23 15:22:06
 */
@Getter
@Setter
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//用户ID
	private Integer userId;
	//用户名
	private String username;
	//手机号
	private String mobile;
	//密码
	transient private String password;
	//创建时间
	private Date createTime;

}
