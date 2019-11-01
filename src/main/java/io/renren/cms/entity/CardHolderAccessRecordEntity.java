package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 名片夹访问记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-11 10:50:49
 */
@Getter
@Setter
public class CardHolderAccessRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 名片夹ID */
	private Integer cardHolderId;
	/** 访问人微信用户ID */
	private String openid;
	/** 访问人头像 */
	private String portrait;
	/** 访问时间 */
	private Date accessTime;
	/**  */
	private Date ctime;
	/** 访问人职位 */
	private String position;
	/** 访问人公司 */
	private String company;
	/** 访问人姓名 */
	private String name;
	/** 名片归属人微信用户ID */
	private String cardHolderOpenid;

}
