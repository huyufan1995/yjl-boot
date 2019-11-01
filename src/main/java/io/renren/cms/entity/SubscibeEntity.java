package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 模板消息
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-06-28 17:06:27
 */
@Getter
@Setter
public class SubscibeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 订阅时间
	private Date ctime;
	// 用户ID
	@NotBlank(message = "openid不能为空")
	private String openid;
	// 订阅ID
	@NotBlank(message = "formid不能为空")
	private String formid;

	// 平台 weixin baidu
	@NotBlank(message = "平台不能为空")
	private String platform;

}
