package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 报名模板使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 17:54:54
 */
@Getter
@Setter
public class TemplateApplyUseRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 报名模板ID */
	private Integer templateApplyId;
	/** 会员ID */
	private Integer memberId;
	/** 模板路径 */
	private String templateImageExample;
	/** 用户头像 */
	private String avatarUrl;
	/** 微信用户ID */
	private String openid;
	/** 使用时间 */
	private Date useTime;

}
