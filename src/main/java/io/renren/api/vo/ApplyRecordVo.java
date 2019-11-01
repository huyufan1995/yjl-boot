package io.renren.api.vo;

import java.util.Date;

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
public class ApplyRecordVo {

	/** 报名记录ID */
	private Integer applyRecordId;
	/** 报名ID */
	private Integer applyId;
	/** 报名人数 */
	private Integer applyCount;

	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 报名活动名称 */
	private String name;

	//未开始 进行中 已结束
	private String status;

	/** 活动描述 */
	private String describe;

	private Integer shareMemberId;

}
