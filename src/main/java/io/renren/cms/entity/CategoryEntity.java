package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 分类
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@Getter
@Setter
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 分类名称
	private String name;
	// 排序号
	private Integer sortNum;
	// 删除标志
	private String isDel;
	// 创建时间
	private Date ctime;
	// 更新时间
	private Date utime;

}
