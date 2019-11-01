package io.renren.cms.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能权益
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-30 15:17:45
 */
@Getter
@Setter
public class FunctionsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 标题 */
	private String title;
	/** 图标 */
	private String image;
	/** 描述 */
	private String describe;
	/** 提示 */
	private String hint;

	private Integer sortNum;

}
