package io.renren.cms.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户标签
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@Getter
@Setter
public class ClienteleTagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 客户ID */
	private Integer clienteleId;
	/** 会员ID */
	private Integer memberId;
	/** 标签ID */
	private Integer tagId;

}
