package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 活动核销人员配置
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-19 11:12:36
 */
@Getter
@Setter
public class ApplyVerifyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 活动id */
	private Integer applyId;
	/**
	 * 会员Code
	 */
	private String code;

	private String portrait;

	private String nickName;

	private String applyTitle;

}
