package io.renren.api.dto;

import java.util.Date;

import io.renren.api.constant.SystemConstant;
import io.renren.enums.CertTypeEnum;
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
	// 角色
	private String role;
	/** 会员类型 common vip */
	private String type = MemberTypeEnum.COMMON.getCode();
	// 手机号
	private String mobile;
	//加密手机号
	private String mobileCipher;
	// 上级ID
	private Integer superiorId;
	/** 头像 */
	private String portrait = SystemConstant.DEFAULT_CARD_PORTRAIT;
	/** 姓名 */
	private String nickname = SystemConstant.DEFAULT_CARD_NAME;

	// 会员开始时间
	private Date startTime;
	// 会员结束时间
	private Date endTime;

	/** 授权状态 */
	private String authStatus = SystemConstant.F_STR;
	/** 认证类型 unknown personage enterprise */
	private String certType = CertTypeEnum.UNKNOWN.getCode();

	/** 允许最大员工数量 */
	private Integer staffMaxCount;

	/** 会员到期天数 */
	private long remainingDay = 0L;
	private int staffCount = 0;
	private Integer deptId;
	private String deptName;
}
