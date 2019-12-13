package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 资讯表
 * 
 * @author moran
 * @date 2019-11-05 11:05:36
 */
@Getter
@Setter
@ApiModel(value = "资讯",description="资讯")
public class InformationsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 资讯标题 */
	@ApiModelProperty(value = "资讯标题")
	private String title;
	/** 资讯内容 */
	@ApiModelProperty(value = "资讯内容")
	private String content;
	/** t:代表逻辑删除,f:不删除 */
	private String isDel;
	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = "审核状态")
	private String auditStatus;
	/**
	 * 审核意见
	 */
	@ApiModelProperty(value = "审核意见")
	private String auditMsg;
	/**
	 * 视频链接地址
	 */
	@ApiModelProperty(value = "视频链接地址")
	private String videoLink;
	/** 创建时间 */
	private Date ctime;
	/**  更新时间*/
	private Date updateTime;
	/**
	 * t是开始f是暂停
	 */
	@ApiModelProperty(value = "t是开始f是暂停")
	private String showStatus;

	/**
	 * 资讯类型
	 */
	@ApiModelProperty(value = "资讯分类",name = "informationType")
	private Integer informationType;

	/**
	 * 资讯内容排版类型  1:纯文字 2：图文 3:有视频
	 */
	@ApiModelProperty(value = "资讯内容排版类型  1:纯文字 2：图文 3:有视频")
	private String contentType;

	/**
	 * 资讯列表文章banner
	 */
	@ApiModelProperty(value = "资讯列表文章banner")
	private String banner;

	/**
	 * 二维码
	 */
	private String qrCode;

    private String informationTypeName;

	/**
	 * 资讯浏览量
	 */
	private Integer browsTotal;

	/**
	 * 排序字段
	 */
	private Integer sort;
}
