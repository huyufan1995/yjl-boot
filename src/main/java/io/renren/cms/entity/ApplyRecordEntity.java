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
	/**  */
	private String applyId;
	/** 活动标题 */
	private String applyTitle;
	/** 报名人openid */
	private String openid;

}
