package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 认证
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
public class CertificateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/**  */
	private Integer memberId;
	/** 认证类型 personage enterprise */
	@NotBlank(message = "认证类型不能为空")
	private String type;
	/** 营业执照 */
	private String license;
	/** 身份证正面 */
	private String identityCardFront;
	/** 手持身份证 */
	private String identityCardFull;
	/** 身份证反面 */
	private String identityCardReverse;
	/** 姓名 */
	@NotBlank(message = "姓名不能为空")
	private String name;
	/** 手机号 */
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	/** 身份证号 */
	private String identityCardNumber;
	/**  */
	private Date ctime;
	/**  */
	private Date utime;
	/**  */
	private String isDel;
	/** 审核状态 pending pass reject */
	private String status;
	/** 拒绝理由 */
	private String reason;

}
