package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 官网模板布局
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 17:50:05
 */
@Getter
@Setter
public class TemplateWebsiteLayoutEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/**  */
	private Date ctime;
	/**  */
	private Date utime;
	/** 模板ID */
	private Integer templateId;
	/** 元数据ID */
	private Integer metadataId;
	/** 会员ID */
	private Integer memberId;
	/** 微信用户ID */
	private String openid;
	/** 布局 */
	private String layout;
	/** 小程序码 */
	private String qrcode;

	//---非持久化属性
	/** 元数据 */
	private String metadata;

}
