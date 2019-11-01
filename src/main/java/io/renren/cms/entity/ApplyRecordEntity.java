package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 报名记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
@Getter
@Setter
public class ApplyRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 报名时间 */
	private Date ctime;
	/**  */
	private String isDel;
	/** 报名模板ID */
	private Integer templateApplyId;
	/** 报名ID */
	@NotNull(message = "报名ID不能为空")
	private Integer applyId;
	/** 报名人openid */
	@NotBlank(message = "报名人不能为空")
	private String joinOpenid;
	/** 报名提交数据明细 */
	@NotBlank(message = "报名内容不能空")
	private String itemDetail;
	/** 报名人头像 */
	private String joinPortrait;
	/** 报名创建人上级memberid */
	private Integer superiorId;
	/** 报名创建人memberid */
	private Integer memberId;
	/** 分享人memberid */
	@NotNull(message = "分享人不能为空")
	private Integer shareMemberId;
	/** 报名人昵称 */
	private String joinNickName;

	//---
	/** 分享人姓名 */
	private String shareMemberName;
	private JSONObject itemDetailJsonObj;

}
