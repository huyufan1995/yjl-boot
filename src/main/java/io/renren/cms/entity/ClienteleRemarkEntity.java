package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户备注
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
public class ClienteleRemarkEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 备注人会员ID */
	private Integer memberId;
	/** 备注内容 */
	@NotBlank(message = "内容不能为空")
	private String content;
	/**  */
	private Date ctime;
	/**  */
	private String isDel;
	/** 客户ID */
	@NotNull(message = "客户不能为空")
	private Integer clienteleId;

	//----非持久化属性
	/** 记录人姓名 */
	private String memberName;

}
