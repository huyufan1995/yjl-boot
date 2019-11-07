package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 资讯表
 * 
 * @author moran
 * @date 2019-11-05 11:05:36
 */
@Getter
@Setter
public class InformationsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 资讯标题 */
	private String title;
	/** 资讯内容 */
	private String content;
	/** t:代表逻辑删除,f:不删除 */
	private String isDel;
	/**
	 * 审核状态
	 */
	private String auditStatus;
	/**
	 * 审核意见
	 */
	private String auditMsg;
	/**
	 * 视频链接地址
	 */
	private String vedioLink;
	/** 创建时间 */
	private Date ctime;
	/**  更新时间*/
	private Date updateTime;
	/**
	 * t是开始f是暂停
	 */
	private String showStatus;

	private Integer type;

}
