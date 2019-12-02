package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-07 17:22:16
 */
@Getter
@Setter
public class InformationTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/**  */
	private String name;
	/** 排名 */
	private Integer sort;

	private Date ctime;

	private String isDel;

}
