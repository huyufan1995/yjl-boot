package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 部门-会员-关系
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
@Getter
@Setter
public class DeptMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 部门ID */
	private Integer deptId;
	/** 会员ID */
	private Integer memberId;
	/** 会员openid */
	private String openid;
	/** 权限 leader 主管 employ 员工 */
	private String perm;
	/** 加入时间 */
	private Date joinTime;

}
