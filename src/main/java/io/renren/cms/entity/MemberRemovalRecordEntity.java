package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 会员迁移记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-06 18:36:05
 */
@Getter
@Setter
public class MemberRemovalRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 迁移时间 */
	private Date ctime;
	/** 操作人 */
	private Integer operationMember;
	/** 源会员 */
	private Integer sourceMember;
	/** 目标迁移会员 */
	private Integer targetMember;

	//非持久化属性
	/** 目标迁移人姓名 */
	private String targetMemberName;

}
