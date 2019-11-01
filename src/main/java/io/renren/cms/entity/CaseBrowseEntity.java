package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 案例库浏览记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
@Getter
@Setter
public class CaseBrowseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 案例ID */
	private Integer caseId;
	/** 浏览人头像 */
	private String browsePortrait;
	/** 浏览人微信用户ID */
	private String browseOpenid;
	/** 浏览时间 */
	private Date browseTime;
	/** 浏览人公司 */
	private String browseCompany;
	/** 浏览人姓名 */
	private String browseName;
	/** 分享人ID */
	private Integer shareMemberId;

	//---
	private String shareRealName;
	private String caseTitle;
	private String creationRealName;

}
