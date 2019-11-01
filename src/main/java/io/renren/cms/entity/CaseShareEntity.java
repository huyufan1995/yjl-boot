package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 案例-分享
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@Getter
@Setter
public class CaseShareEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 案例ID */
	private Integer caseId;
	/** 分享人会员ID */
	private Integer shareMemberId;
	private Integer shareSuperiorId;
	/** 分享时间 */
	private Date shareTime;

}
