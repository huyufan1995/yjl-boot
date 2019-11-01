package io.renren.cms.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 部门
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
@Getter
@Setter
public class DeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 部门名称 */
	private String name;
	/**  */
	private Integer superiorId;
	/**  */
	private String isDel;

	//----
	/** 部门人数 */
	private int teamCnt;

}
