package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 案例
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@Getter
@Setter
public class CaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/**  */
	@NotBlank(message = "标题不能为空")
	private String title;
	/** 浏览量 */
	private Integer viewCnt;
	/** 会员ID */
	private Integer memberId;
	/** 上级会员ID */
	private Integer superiorId;
	/** 创建时间 */
	private Date ctime;
	/** 更新时间 */
	private Date utime;
	/** 删除标志 */
	private String isDel;
	/** 详情JSON */
	@NotBlank(message = "详细不能为空")
	private String details;

	private String intro;

}
