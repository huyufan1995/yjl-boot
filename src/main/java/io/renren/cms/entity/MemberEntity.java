package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 会员
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 头像 */
	private String portrait;
	/** 昵称 */
	private String nickname;
	/** 性别 man woman */
	private String gender;
	/**  */
	private Date ctime;
	/** 角色 boss admin staff */
	private String role;
	/** 删除标志 t f */
	private String isDel;
	/** 状态 normal freeze */
	private String status;
	/** 上级ID */
	private Integer superiorId;
	/** 姓名 */
	private String realName;
	/** 会员开始时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	/** 会员结束时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	/** 手机号 */
	private String mobile;
	/** 微信用户ID */
	private String openid;
	/** 认证类型 unknown personage enterprise */
	private String certType;
	/** 会员类型 common vip */
	private String type;
	/** 授权状态 t or f */
	private String authStatus;
	/** 领取码 */
	private String code;
	/** 绑定状态 not未绑定 unbind已解绑 bind已经绑定 */
	private String bindStatus;
	/** 允许最大员工数量 */
	private Integer staffMaxCount;
	/** 公司 */
	private String company;

}
