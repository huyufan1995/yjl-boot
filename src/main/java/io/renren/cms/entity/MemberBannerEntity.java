package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 会员banner
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-18 11:15:13
 */
@Getter
@Setter
public class MemberBannerEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 会员Banner */
	private String memberBanner;
	/** 会员id */
	private Integer memberId;

}
