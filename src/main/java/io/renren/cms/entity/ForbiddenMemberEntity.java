package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 封禁实体类
 *
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForbiddenMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/**
	 * 用户OpenID
	 */
	private String openId;

	/**
	 * 封禁信息
	 */
	private String forbiddenMsg;

	/**
	 * 封禁日期
	 */
	private Date createDate;

	/**
	 * 父类ID
	 */
	private Integer superiorId;
}
