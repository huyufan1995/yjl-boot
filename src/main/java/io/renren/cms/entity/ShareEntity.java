package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 分享表
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-11-05 11:05:36
 */
@Getter
@Setter
public class ShareEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 分享人openid */
	private String sharePeople;
	/** 分享时间 */
	private Date createTime;
	/** 资讯Id */
	private Integer informationId;

}
