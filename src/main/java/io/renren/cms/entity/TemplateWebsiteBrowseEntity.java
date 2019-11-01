package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 官网模板浏览记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-04 10:30:08
 */
@Getter
@Setter
public class TemplateWebsiteBrowseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 官网模板ID */
	private Integer templateWebsiteId;
	/** 被访问人微信用户ID */
	private String ownerOpenid;
	/** 用户头像 */
	private String portrait;
	/** 访问人人微信用户ID */
	private String browseOpenid;
	/** 访问时间 */
	private Date accessTime;
	/** 访问人公司  */
	private String company;
	/** 访问人职位 */
	private String position;
	/** 访问人姓名 */
	private String name;
	/** 创建时间 */
	private Date ctime;
	/** 所有者头像 */
	private String ownerPortrait;
	/** 所有者公司 */
	private String ownerCompany;

	private String shareRealName;

}
