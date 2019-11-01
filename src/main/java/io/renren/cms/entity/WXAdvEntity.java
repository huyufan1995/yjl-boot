package io.renren.cms.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 微信广告
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2018-06-27 11:58:28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WXAdvEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	// 广告代码
	private String unitId;
	// 位置
	private String location;
	// 是否显示
	private String isShow;

}
