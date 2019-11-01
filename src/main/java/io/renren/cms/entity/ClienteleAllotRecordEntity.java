package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 客户分片记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-29 17:16:27
 */
@Getter
@Setter
public class ClienteleAllotRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 客户ID */
	private Integer clienteleId;
	/** 操作时间 */
	private Date ctime;
	/** 操作人 */
	private Integer operationMember;
	/** 源负责人 */
	private Integer sourceMember;
	/** 目标负责人 */
	private Integer targetMember;

}
