package io.renren.api.vo;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 * 案例
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@Getter
@Setter
public class CaseVo {

	/**  */
	private Integer id;
	/**  */
	private String title;
	/** 浏览量 */
	private Integer viewCnt;
	/** 会员ID */
	private Integer memberId;
	/** 上级会员ID */
	private Integer superiorId;
	/** 创建时间 */
	private Date ctime;
	/** 更新时间 */
	private Date utime;
	/** 详情JSON */
	private String details;

	private List<String> browsePortraits = Lists.newArrayList();
	private boolean praise;
	private String intro;
	private int shareCnt;
	private int praiseCnt;
	private int browseCnt;

	private String realName;
	private String mobile;
	/** 是否可编辑 */
	private boolean edit;

}
