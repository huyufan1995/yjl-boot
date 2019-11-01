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
public class UserDaysEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;//权限ID
	//
	private String openId;//微信用户openId
	//总共天数
	private Integer totalDays;//用户总共可以使用天数
	//
	private Date registeTime;//注册时间
	
	private Date createTime;//权限生成时间
	//
	private Date updateTime;//权限修改时间
	//是否删除（t-删除；f-未删除）
	private String ifDeleted;////权限是否删除
	
	
	private Integer remain_days;//权限剩余使用天数
	private Integer add_days;//权限剩余使用天数

	
	public Integer getAdd_days() {
		return add_days;
	}
	public void setAdd_days(Integer add_days) {
		this.add_days = add_days;
	}
	public Integer getRemain_days() {
		return remain_days;
	}
	public void setRemain_days(Integer remain_days) {
		this.remain_days = remain_days;
	}
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
	 * 设置：总共天数
	 */
	public void setTotalDays(Integer totalDays) {
		this.totalDays = totalDays;
	}
	/**
	 * 获取：总共天数
	 */
	public Integer getTotalDays() {
		return totalDays;
	}
	
	public Date getRegisteTime() {
		return registeTime;
	}
	public void setRegisteTime(Date registeTime) {
		this.registeTime = registeTime;
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
