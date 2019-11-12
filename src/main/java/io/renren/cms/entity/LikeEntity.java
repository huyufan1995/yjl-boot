package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 点赞表
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 10:03:39
 */
@Getter
@Setter
public class LikeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 点赞人openid */
	private String openid;
	/** 点赞时间 */
	private Date ctime;
	/** 数据ID */
	private Integer dataId;
	/** 点赞类型  1：资讯 2：评论 3：活动 */
	private Integer likeType;

}
