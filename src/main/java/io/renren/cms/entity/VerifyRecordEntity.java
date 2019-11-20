package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 核销记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:37
 */
@Getter
@Setter
public class VerifyRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 活动Id */
	private Integer applyId;
	/** 核销人openid */
	private String openid;
	/** 参加活动人memberId */
	private Integer memberId;
	/** 核显时间 */
	private Date ctime;

}
