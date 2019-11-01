package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 点赞记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-29 16:03:00
 */
public class PraiseRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private Integer templateId;
	//
	private String openId;
	
	private String praiseStatus;
	//
	private Date createTime;
	
	private String nickName;
	private String avatarUrl;
	

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
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
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	/**
	 * 获取：
	 */
	public Integer getTemplateId() {
		return templateId;
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
	
	public String getPraiseStatus() {
		return praiseStatus;
	}
	public void setPraiseStatus(String praiseStatus) {
		this.praiseStatus = praiseStatus;
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
}
