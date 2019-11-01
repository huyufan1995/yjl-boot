package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 留言
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@Getter
@Setter
public class LeaveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 留言内容 */
	@NotBlank(message = "内容不能为空")
	private String content;
	/**  */
	@NotBlank(message = "openid不能为空")
	private String openid;
	/**  */
	private Date ctime;
	/** 留言人头像 */
	private String portrait;
	/** 留言人姓名 */
	@NotBlank(message = "姓名不能为空")
	private String name;
	/** 手机号 */
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	/** 状态 */
	private String status;
	/**  */
	private String isDel;
	/** 会员ID */
	@NotNull(message = "接收人不能为空")
	private Integer memberId;

	//---
	private String realName;

}
