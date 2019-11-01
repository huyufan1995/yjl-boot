package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 名片
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
public class CardEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 姓名 */
	@NotBlank(message = "姓名不能为空")
	private String name;
	/** 头像 */
	private String portrait;
	/** 职位 */
	private String position;
	/** 公司 */
	@NotBlank(message = "公司不能为空")
	private String company;
	/** 手机号 */
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	/**  */
	private Date ctime;
	/**  */
	private Date utime;
	/**  */
	private String isDel;
	/** 名片二维码 */
	private String qrcode;
	/** 性别 man woman */
	private String gender;
	/** 微信号 */
	private String weixin;
	/** 邮箱 */
	private String email;
	/** 地址 */
	private String address;
	/** 自我介绍 */
	private String introduce;
	/**  */
	private Integer memberId;
	/** 名称首字母 */
	private String firstLetter;
	/** 微信用户ID */
	@NotBlank(message = "openid不能为空")
	private String openid;
	/** 中心经度 */
	private String addressLongitude;
	/** 中心纬度 */
	private String addressLatitude;

}
