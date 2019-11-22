package io.renren.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-04-23 16:03:22
 */
@Getter
@Setter
public class TemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 模板名称
	private String name;
	// 模板路径
	private String imageTemplate;
	// 实例路径
	private String imageExample;
	// 分类
	private Integer categoryId;
	// 删除标志
	private String isDel;
	// 创建时间
	private Date ctime;
	// 更新时间
	private Date utime;
	// 使用量
	private Integer useCnt;
	// 浏览量
	private Integer viewCnt;
	// 点赞量
	private Integer praiseCnt;
	// 是否免费
	private String isFree;
	// 模板宽
	private Integer width;
	// 模板高
	private Integer height;
	//是否发布
	private String isRelease;
	//是否是趣图（f-不是；t-是）
	private String isFun;
	// 非持久化
	private List<UseRecordEntity> useRecords;
	
	private CategoryEntity categoryEntity;
	
	
	private Integer sortNum;//排第几
	private Integer praiseNum;//点赞人数
	private String ifPraise;//用户是否点赞：t-点赞；f-为点赞
	private List<String> headerImg;
	
}
