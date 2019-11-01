package io.renren.api.vo;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

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
public class ApplyVo {

	/**  */
	private Integer id;
	/** 创建人openid */
	private String openid;
	/** 创建了上级memberid */
	private Integer superiorId;
	/** 创建了memberid */
	private Integer memberId;
	/** 申请人数 */
	private Integer applyCount;
	/** 报名模板ID */
	private Integer templateApplyId;
	/** 名称标题 */
	private String name;
	/** 描述 */
	private String describe;
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 图片集 */
	private String images;
	/** 表单项 */
	private String items;
	/** 权限 public private */
	private String permission;
	/** 地址 */
	private String address;
	/** 中心经度 */
	private String addressLongitude;
	/** 中心纬度 */
	private String addressLatitude;

	//未开始 进行中 已结束
	private String status;
	//是否可编辑
	private boolean edit;
	/** 布局 */
	private String layout;

	/** 是否参加报名 */
	private boolean join;
	private List<String> joinPortraits = Lists.newArrayList();
	/** 发起人姓名 */
	private String realName;
	/** 样图 */
	private String examplePath;

	private Integer recordCount;

}
