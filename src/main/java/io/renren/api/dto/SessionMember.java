package io.renren.api.dto;

import java.util.Date;

import io.renren.api.constant.SystemConstant;
import io.renren.enums.MemberTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SessionMember {

	// 会员ID
	private Integer memberId;
	// 微信ID
	private String openid;
	// 令牌
	private String token;
	/** 会员类型 common vip*/
	private String type = MemberTypeEnum.COMMON.getCode();
	// 手机号
	private String mobile;
	//加密手机号
	private String mobileCipher;
	/** 头像 */
	private String portrait;
	/** 姓名 */
	private String nickname;
	/**
	 * 是否展示vip logo
	 */
	private String showVip;

	/**
	 * 二维码
	 */
	private String qrCode;

	/**
	 * vipCode
	 */
	private String code;
}
