package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 官网模板元数据
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-03 16:59:43
 */
@Getter
@Setter
public class TemplateWebsiteMetadataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/**  */
	private Date ctime;
	/**  */
	private Date utime;
	/** 元数据JSON */
	private String metadata;
	/** 会员ID */
	private Integer memberId;
	/** 最后修改人会员ID */
	private Integer operator;

}
