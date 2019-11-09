package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 评论表
 * 
 * @author moran
 * @date 2019-11-05 11:05:36
 */
@Getter
@Setter
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 资讯ID */
	private String informationId;
	/**  评论内容 */
	private String remark;
	/**  评论时间 */
	private Date ctime;
	/** 评论人openid */
	private String openid;

}
