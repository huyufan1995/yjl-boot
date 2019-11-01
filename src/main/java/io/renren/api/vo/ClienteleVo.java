package io.renren.api.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.cms.entity.TagEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
public class ClienteleVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 姓名 */
	private String name;
	/**  */
	private String mobile;
	/** 头像 */
	private String portrait;
	/**  */
	private Date ctime;
	/** 公司 */
	private String company;
	/** 职位 */
	private String position;
	/** 负责人 */
	private Integer memberId;

	private List<TagEntity> tags = Lists.newArrayList();
	private List<ClienteleRemarkEntity> remarks = Lists.newArrayList();
	//负责人姓名
	private String memberName;
	//分配历史
	private List<ClienteleAllotRecordVo> allotRecord = Lists.newArrayList();
	/** 客户来源 */
	private String source;

}
