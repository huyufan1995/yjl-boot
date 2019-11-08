package io.renren.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * banner
 * 
 * @author moran
 * @date 2019-11-8
 */
@Getter
@Setter
public class BannerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 图片路径
	private String imagePath;
	// 类型 img app h5
	private String type;
	// 序号
	private Integer sortNum;
	// 值
	private String bannerVal;
	/** 小程序页面路径 */
	private String appPath;
	/** 位置 index crm */
	private String location;

}
