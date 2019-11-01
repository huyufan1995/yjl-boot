package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 留言-案例库
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
@Getter
@Setter
public class CaseLeaveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 留言内容 */
	@NotBlank(message = "内容不能为空")
	private String content;
	/** 留言人openid */
	@NotBlank(message = "留言人不能为空")
	private String openid;
	/**  */
	private Date ctime;
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
	/** 案例ID */
	@NotNull(message = "案例不能为空")
	private Integer caseId;
	/** 分享人ID */
	@NotNull(message = "分享人不能为空")
	private Integer shareMemberId;

	//---
	private String shareRealName;
	private String caseTitle;
	private String creationRealName;

}
