package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 使用记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@Getter
@Setter
public class UseRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 模板名称
	private String templateName;
	// 模板ID
	private Integer templateId;
	// 模板路径
	private String templateImageExample;
	// 用户头像
	private String avatarUrl;
	// 用户ID
	private String openid;
	// 使用时间
	private Date useTime;
	// 耗时
	private long consumeTime;
	// 结果图片
	private String templateImageResult;

	private Integer width;// 模板宽

	private Integer height;// 模板高

	private String type;

	// ----
	private TemplateEntity templateEntity;
	private String templateUrl;// 模板底图
	private String ifPraise;// 用户是否点赞：t-点赞；f-为点赞

}
