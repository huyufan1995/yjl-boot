package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 案例-赞
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@Getter
@Setter
public class CasePraiseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/**  */
	private Date ctime;
	/**  */
	private String openid;
	/** 案例ID */
	private Integer caseId;

}
