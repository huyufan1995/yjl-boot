package io.renren.cms.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@Getter
@Setter
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Integer id;
	/** 订单编号 */
	private String orderSn;
	/** 系统会员ID */
	private Integer memberId;
	/** 支付时间 */
	private Date paymentTime;
	/** 支付金额 */
	private BigDecimal payMoney;
	/** 百度用户ID */
	private String baiduUserId;
	/** 百度订单ID */
	private String baiduOrderId;
	/** 平台 weixin baidu ali */
	private String platform;
	/** 状态 non or paid */
	private String status;
	/** 订单类型 newbuy upgrade renewal */
	private String type;
	/** 订单明细 json */
	private String detail;
	/** 微信用户ID */
	private String weixinOpenid;
	private Date ctime;

}
