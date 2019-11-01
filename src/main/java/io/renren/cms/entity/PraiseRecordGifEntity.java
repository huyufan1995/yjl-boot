package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 动图点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
@Getter
@Setter
public class PraiseRecordGifEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	//
	private Integer templateId;
	//
	private String openId;
	//
	private Date ctime;

}
