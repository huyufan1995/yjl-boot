package io.renren.enums;

/**
 * 接口返回码
 * @author yujia
 *
 */
public enum ResponseCodeEnum {
	
	
	SMS_SEND_ERROR(100000, "短信发送失败"),
	SMS_MOBILE_FORMAT_ERROR(100001, "手机号格式错误"),
	
	SEC_MSG_ILLEGALITY(90000, "内容含有违法违规内容"),
	SEC_IMG_ILLEGALITY(90001, "图片含有违法违规内容"),
	
	GOODS_NONENTITY(80000, "商品已下架"),
	GOODS_NO_PERMISSIONS_BUY(80001, "VIP专属商品,您无权购买"),
	GOODS_NOT_VIP(80002, "不是VIP专属商品"),
	
	ADDRESS_DEFAULT_DELETE_ERROR(70000, "默认地址不能删除"),
	
	CONFIG_IS_NULL_ERROR(60000, "没找到配置项"),
	
	MONEY_DISCREPANCY(50000, "金额不符"),
	
	CART_CLEAR_ERROR(40000, "清空购物袋错误"),
	
	TOKEN_INVALID(30000, "登录失效，请重新登录"),
	
	MEMBER_FREEZED(20000, "账号被冻结"),
	MEMBER_NOT_VIP(20001, "不是VIP会员"),
	MEMBER_VIP_EXPIRE(20002, "VIP已到期"),
	MEMBER_GIVE_CNT_EXHAUST(20003, "赠送次数已用尽"),
	MEMBER_IS_VIP(20004, "您已是VIP会员，无法领取"),
	MEMBER_UNAUTHORIZED(20005, "用户未授权"),
	MEMBER_VIP_GIVE_ONESELF(20006, "不能赠送自己"),
	MEMBER_NONENTITY(20007, "会员不存在"),
	
	ORDER_INEXISTENCE(10000, "订单不存在"),
	ORDER_FINISH(10001, "订单已完成"),
	ORDER_STATUS_WRONG(10002, "订单状态错误"),
	ORDER_INVALID(10003, "订单已失效，请重新下单"),
	
	/**
	 * 积分错误
	 */
	INTEGRAL_INSUFFICIENT(9000, "积分不足"),
	INTEGRAL_SAVE_ERROR(9001, "添加积分错误"),
	INTEGRAL_UPDATE_ERROR(9002, "更新积分错误"),
	INTEGRAL_DETAIL_SAVE_ERROR(9003, "添加积分明细错误"),
	
	SIGNIN_EXIST(8000, "今天已签到"),
	
	SECKILL_OVER(7000, "活动已结束"),
	SECKILL_LIMITATION_EXCEED(7001, "超出限购数量"),
	
	INVENTORY_INSUFFICIENT(6000, "库存不足"),
	
	
	ISSUE_JOIN_EXISTED(5000, "已参加"),
	/**
	 * 文件上传
	 */
	FILE_READING_ERROR(4001, "FILE_READING_ERROR!"),
	FILE_NOT_FOUND(4002, "FILE_NOT_FOUND!"),
	UPLOAD_ERROR(4003, "上传图片出错"),
	EXCEED_LIMIT(4004, "超出限制"),
	
	NO_PERMITION(3000, "权限错误"),
	
	DATA_ISNULL(2000, "数据不存在"),
	
	PARAMETER_ILLEGALITY(1000, "参数非法"),
	PARAMETER_IDENTITY_CARD_ILLEGALITY(1001, "身份证号格式非法"),
	PARAMETER_REAL_NAME_ILLEGALITY(1003, "姓名格式非法"),
	
	/**
	 * 字典
	 */
	DICT_EXISTED(400,"字典已经存在"),
	ERROR_CREATE_DICT(500,"创建字典失败"),
	ERROR_WRAPPER_FIELD(500,"包装字典属性失败"),


	/**
	 * 权限和数据问题
	 */
	DB_RESOURCE_NULL(400,"数据库中没有该资源"),
	REQUEST_INVALIDATE(400,"请求数据格式不正确"),
	INVALID_KAPTCHA(400,"验证码不正确"),
	CANT_DELETE_ADMIN(600,"不能删除超级管理员"),
	CANT_FREEZE_ADMIN(600,"不能冻结超级管理员"),
	CANT_CHANGE_ADMIN(600,"不能修改超级管理员角色"),

	/**
	 * 账户问题
	 */
	USER_ALREADY_REG(401,"该用户已经注册"),
	NO_THIS_USER(400,"没有此用户"),
	USER_NOT_EXISTED(400, "没有此用户"),
	ACCOUNT_FREEZED(401, "账号被冻结"),
	OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
	TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),

	/**
	 * 错误的请求
	 */
	REQUEST_NULL(400, "请求有错误"),
	SERVER_ERROR(500, "服务器异常");

	ResponseCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private int code = 0;
	private String msg = "succeed";
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	

}
