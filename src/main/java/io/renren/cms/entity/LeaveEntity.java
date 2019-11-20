package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 留言
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 14:32:10
 */
@Getter
@Setter
public class LeaveEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 留言内容 */
	private String content;
	/**  */
	private String openid;
	/**  */
	private Date ctime;
	/** 状态 */
	private String status;
	/**  */
	private String isDel;
	/** 会员ID */
	private Integer memberId;

}
