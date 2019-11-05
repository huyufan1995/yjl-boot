package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 首页数据天数
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-05-21 11:35:37
 */
public class SortEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private Integer sortDays;
	
	private String type;//分类（最热排序天数-hot；精选排序天数-praise,;推荐查询条数-parisesearch）
	//
	private Date updateTime;

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
	public void setSortDays(Integer sortDays) {
		this.sortDays = sortDays;
	}
	/**
	 * 获取：
	 */
	public Integer getSortDays() {
		return sortDays;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
}
