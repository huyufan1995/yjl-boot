package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;



/**
 * 转发记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:36
 */
@Data
public class GroupRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String openId;
	//转发方式（group-群，personal-个人）
	private String type;
	//
	private Date createTime;
	
	private WxUserEntity wxUserEntity;
	
	private int group_cnt;

}
