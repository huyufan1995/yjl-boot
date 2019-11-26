package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-25 10:59:15
 */
@Getter
@Setter
public class ApplyBannerEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 活动banner图 */
	private String bannerImg;
	/** 活动Id */
	private Integer applyId;

	private String applyTitle;

}
