package io.renren.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-24 12:27:52
 */
@Getter
@Setter
public class ArticleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 标题
	private String title;
	// 内容
	private String content;
	// 创建时间
	private Date ctime;
	// 更新时间
	private Date utime;

}
