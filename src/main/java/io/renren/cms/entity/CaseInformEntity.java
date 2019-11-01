package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * 案例-举报
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-10 14:58:35
 */
@Getter
@Setter
public class CaseInformEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 举报人openid */
	@NotBlank(message = "举报人不能为空")
	private String openid;
	/** 举报类型 */
	@NotBlank(message = "举报类型不能为空")
	private String type;
	/**  */
	private Date ctime;
	/** 案例ID */
	@NotNull(message = "举报案例不能为空")
	private Integer caseId;

}
