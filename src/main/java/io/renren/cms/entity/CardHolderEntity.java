package io.renren.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 * 名片夹
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 17:23:12
 */
@Getter
@Setter
public class CardHolderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 姓名 */
	@NotBlank(message = "姓名不能为空")
	private String name;
	/** 头像 */
	private String portrait;
	/** 职位 */
	private String position;
	/** 公司 */
	@NotBlank(message = "公司不能为空")
	private String company;
	/** 手机号 */
	@NotBlank(message = "手机号不能为空")
	private String mobile;
	/**  */
	private Date ctime;
	/**  */
	private Date utime;
	/**  */
	private String isDel;
	/** 性别 man woman */
	private String gender;
	/** 微信号 */
	private String weixin;
	/** 邮箱 */
	private String email;
	/** 地址 */
	private String address;
	/**  */
	private Integer memberId;
	/** 名称首字母 */
	private String firstLetter;
	/** 创建者openid */
	private String openid;
	/** 中心经度 */
	private String addressLongitude;
	/** 中心纬度 */
	private String addressLatitude;

	//非持久化
	/** 访问人头像 */
	private List<String> accessPortraits = Lists.newArrayList();

	/** 是否已保存到crm */
	private boolean saveCrm = false;

}
