package io.renren.api.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

import io.renren.enums.CertTypeEnum;
import io.renren.enums.MemberTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 名片
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
public class CardVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 姓名 */
	private String name;
	/** 头像 */
	private String portrait;
	/** 职位 */
	private String position;
	/** 公司 */
	private String company;
	/** 手机号 */
	private String mobile;
	/** 名片二维码 */
	private String qrcode;
	/** 性别 man woman */
	private String gender;
	/** 微信号 */
	private String weixin;
	/** 邮箱 */
	private String email;
	/** 地址 */
	private String address;
	/** 自我介绍 */
	private String introduce;
	/**会员类型 common vip*/
	private String type = MemberTypeEnum.COMMON.getCode();
	/** 访问人头像 */
	private List<String> accessPortraits = Lists.newArrayList();
	/** 访问人数 */
	private int accessCount;
	/** 认证类型 unknown personage enterprise */
	private String certType = CertTypeEnum.UNKNOWN.getCode();
	/** 中心经度 */
	private String addressLongitude;
	/** 中心纬度 */
	private String addressLatitude;
	private String openid;

}
