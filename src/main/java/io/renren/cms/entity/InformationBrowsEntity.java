package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 资讯浏览记录
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 14:39:16
 */
@Getter
@Setter
public class InformationBrowsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 创建时间 */
	private Date ctime;
	/** 资讯浏览记录人openid */
	private String openid;
	/** 资讯Id */
	private String informationId;

}
