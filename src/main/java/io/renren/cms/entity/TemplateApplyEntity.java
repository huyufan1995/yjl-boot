package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 * 报名模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:45
 */
@Getter
@Setter
public class TemplateApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/**  */
	private Date ctime;
	/**  */
	private Date utime;
	/**  */
	private String isDel;
	/** 是否发布 */
	private String isRelease;
	/** 初始使用量 */
	private Integer initUseCnt;
	/** 使用量 */
	private Integer useCnt;
	/** 样图 */
	private String examplePath;
	/** 布局 */
	private String layout;

	//---
	private List<String> usePortrait = Lists.newArrayList();

}
