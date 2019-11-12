package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 活动回顾
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-12 17:49:58
 */
@Getter
@Setter
public class ApplyReviewEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 活动id */
	private Integer applyId;
	/** 活动回顾 */
	private String applyReviewContent;
	/** t:展示 f:暂停 */
	private String showStatus;
	/** pass:通过reject:不通过pending:审核中，uncommit未提交审核 */
	private String auditStatus;
	/** 审核意见 */
	private String auditMsg;
	/** 1:text文本 2：image图片 3video视频 */
	private String applyReviewType;
	/** 视频链接 */
	private String videoLink;
	/**  */
	private Date ctime;
	/** t:是 f:否 */
	private String isDel;

}
