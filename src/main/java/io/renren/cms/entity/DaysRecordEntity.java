package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-18 15:59:46
 */
public class DaysRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String openId;
	//获取使用天数
	private Integer days;
	//规则类别（registe-新用户注册；send-转发发；old-旧用户数据维护;cms-后台手动）
	private String type;
	//
	private Date createTime;
	//
	private Date updateTime;
	//是否删除（t-删除；f-未删除）
	private String ifDeleted;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	/**
	 * 获取：
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * 设置：获取使用天数
	 */
	public void setDays(Integer days) {
		this.days = days;
	}
	/**
	 * 获取：获取使用天数
	 */
	public Integer getDays() {
		return days;
	}
	/**
	 * 设置：规则类别（registe-新用户注册；send-转发）
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：规则类别（registe-新用户注册；send-转发）
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：是否删除（t-删除；f-未删除）
	 */
	public void setIfDeleted(String ifDeleted) {
		this.ifDeleted = ifDeleted;
	}
	/**
	 * 获取：是否删除（t-删除；f-未删除）
	 */
	public String getIfDeleted() {
		return ifDeleted;
	}
}
