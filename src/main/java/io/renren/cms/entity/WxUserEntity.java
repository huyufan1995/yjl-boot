package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信用户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@Getter
@Setter
public class WxUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	//
	private String openId;
	// 用户昵称
	private String nickName;
	// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String gender;
	// 语言
	private String language;
	// 城市
	private String city;
	// 省份
	private String province;
	// 国家
	private String country;
	// 用户头像
	private String avatarUrl;
	//
	private String unionId;
	//
	private String appid;
	//
	private String sessionKey;
	// 手机号
	private String mobile;
	// 最后登入时间
	private Date loginTime;
	//
	private Date ctime;

}
