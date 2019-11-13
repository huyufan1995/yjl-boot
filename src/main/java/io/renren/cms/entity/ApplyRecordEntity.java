package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 活动报名记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 09:58:16
 */
@Getter
@Setter
public class ApplyRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 活动Id */
	private String applyId;
	/** 报名人openid */
	private String openid;

	/**
	 * 创建时间
	 */
	private Date ctime;

}
