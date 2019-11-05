package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 点赞表
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
@Getter
@Setter
public class LikeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 点赞人openid */
	private String createPeople;
	/** 点赞时间 */
	private Date createTime;
	/** 资讯Id */
	private Integer informationId;

}
