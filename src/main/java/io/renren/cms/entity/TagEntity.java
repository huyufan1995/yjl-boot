package io.renren.cms.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 标签
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@Getter
@Setter
public class TagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 会员ID */
	private Integer memberId;
	/** 上级领导会员ID */
	private Integer superiorId;
	/** 标签名称 */
	private String name;

}
