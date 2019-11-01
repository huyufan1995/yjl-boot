package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 客户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
@Accessors(chain = true)
public class ClienteleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 姓名 */
	@NotBlank(message = "姓名不能为空")
	private String name;
	/**  */
	@NotBlank(message = "手机不能为空")
	private String mobile;
	/** 头像 */
	private String portrait;
	/**  */
	private Date ctime;
	/** 公司 */
	@NotBlank(message = "公司不能为空")
	private String company;
	/** 职位 */
	@NotBlank(message = "职位不能为空")
	private String position;
	/** 负责人 */
	private Integer memberId;
	/**  */
	private String isDel;
	/** 负责人上级ID */
	private Integer superiorId;
	/** 客户来源 */
	private String source;

}
