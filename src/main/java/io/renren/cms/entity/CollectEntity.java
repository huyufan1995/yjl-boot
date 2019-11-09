package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 收藏
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-09 17:25:18
 */
@Getter
@Setter
public class CollectEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/**  */
	private Integer dataId;
	/** 1:资讯 2:活动 3:会员 */
	private Integer collectType;
	/** 会员openId */
	private String openid;

}
