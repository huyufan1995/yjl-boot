package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 动图模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-07-18 10:17:40
 */
@Getter
@Setter
public class GifTemplateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	//
	private Date ctime;
	//
	private Date utime;
	//
	private String isDel;
	//
	private String name;
	//
	private String cover;
	//
	private String isRelease;
	//
	private Integer useCnt;
	//
	private String examplePath;
	//
	private String videoPath;
	//
	private String assPath;
	//
	private String sentences;

	private Integer width;// 模板宽

	private Integer height;// 模板高

}
