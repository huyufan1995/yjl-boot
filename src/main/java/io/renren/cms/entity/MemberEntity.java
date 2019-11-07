package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 会员
 * 
 * @author moran
 * @date 2019-11-05 15:22:44
 */
@Getter
@Setter
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
	/** 创建时间 */
	private Date ctime;
	/** 删除标志 t f */
	private String isDel;
	/** 状态 normal freeze */
	private String status;
	/** 姓名 */
	private String realName;
	/** 手机号 */
	private String mobile;
	/** 微信用户ID */
	private String openid;
	/** 会员类型 common vip */
	private String type;
	/** 公司名称 */
	private String company;
	/** 邮箱 */
	private String email;

	private String showVip;

}
