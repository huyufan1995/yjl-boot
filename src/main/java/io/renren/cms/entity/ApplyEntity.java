package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.renren.api.dto.ApplyRecordEntiyDto;
import io.renren.api.dto.ApplyReviewEntityDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-11 19:02:39
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 活动标题 */
	private String applyTitle;
	/** 活动开始时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	private Date startTime;
	/** 活动结束时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
	private Date endTime;
	/** 创建时间 */
	private Date ctime;
	/** 活动地址 */
	private String applyLocation;
	/** 创建人 */
	private String createPeople;
	/** 活动详情 */
	private String applyContent;
	/** t:逻辑删除 f:没有删除 */
	private String isDel;

	/**
	 * 活动banner
	 */
	private String banner;

	/**
	 * pass:通过reject:不通过pending:审核中，uncommit未提交审核
	 */
	private String auditStatus;

	/**
	 * t:展示 f:暂停
	 */
	private String showStatus;

	/**
	 *  活动详情类型 1:text文本 2：image图片 3video视频
	 */
	private String applyContentType;

	/**
	 * 视频链接地址
	 */
	private String videoLink;

	/**
	 * 审核意见
	 */
	private String auditMsg;

	/**
	 *活动热度
	 */
	private Integer applyHot;


	/**
	 * 活动报名记录
	 */
	private List<ApplyRecordEntiyDto> applyRecordEntiyDto;
}
