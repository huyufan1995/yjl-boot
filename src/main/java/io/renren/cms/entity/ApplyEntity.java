package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 报名
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
@Getter
@Setter
public class ApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 创建时间 */
	private Date ctime;
	/** 更新时间 */
	private Date utime;
	/** 创建人openid */
	private String openid;
	/** 创建了上级memberid */
	private Integer superiorId;
	/** 创建了memberid */
	private Integer memberId;
	/** 申请人数 */
	private Integer applyCount;
	/** 报名模板ID */
	@NotNull(message = "模板不能为空")
	private Integer templateApplyId;
	/** 名称标题 */
	@NotBlank(message = "活动名称不能为空")
	private String name;
	/** 描述 */
	@NotBlank(message = "活动描述不能为空")
	private String describe;
	/** 开始时间 */
	@NotNull(message = "活动开始时间不能空")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	/** 结束时间 */
	@NotNull(message = "活动结束时间不能空")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	/** 图片集 */
	private String images;
	/** 表单项 */
	private String items;
	/** 权限 public private */
	@NotBlank(message = "活动类型不能为空")
	private String permission;
	/** 删除标志 */
	private String isDel;
	/** 地址 */
	private String address;
	/** 中心经度 */
	private String addressLongitude;
	/** 中心纬度 */
	private String addressLatitude;
	/** 浏览数量 */
	private Integer viewCount;

	//---非持久化
	/** 发起人姓名 */
	private String realName;

	private Integer recordCount;

}
