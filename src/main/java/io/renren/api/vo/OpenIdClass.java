package io.renren.api.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信用户
 *
 */
@Getter
@Setter
public class OpenIdClass {
	private String access_token;
	private String expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	private String unionid;
	private String session_key;
	private String errcode;
	private String errmsg;
}
