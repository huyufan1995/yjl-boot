package io.renren.api.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.constant.WxPayConstants.SignType;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.OrderDetailDto;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.OrderEntity;
import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.OrderService;
import io.renren.cms.service.SubscibeService;
import io.renren.enums.BindStatusEnum;
import io.renren.enums.MemberRoleEnum;
import io.renren.enums.MemberTypeEnum;
import io.renren.enums.OrderTypeEnum;
import io.renren.service.SysConfigService;
import io.renren.utils.HttpContextUtils;
import io.renren.utils.ProjectUtils;
import io.renren.utils.SystemCache;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Api("支付")
@RestController
@RequestMapping("/api/pay")
public class ApiPayController {

	@Value("${yykj.paynotifyurl}")
	private String paynotifyurl;

	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private SubscibeService subscibeService;

	@IgnoreAuth
	@PostMapping("/newbuy")
	@ApiOperation(value = "新购")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "mobile", value = "手机号", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "验证码", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "quantity", value = "开通账号数量", required = true),
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "time", value = "开通年限（年）", required = true) })
	public ApiResult newbuy(@RequestParam String openid, @RequestParam String mobile, @RequestParam String code,
			@RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
			@RequestParam(value = "time", defaultValue = "1") Integer time) throws WxPayException {

		if (!Validator.isMobile(mobile)) {
			return ApiResult.error(500, "手机号格式错误");
		}
		
		if (quantity <= 0 || time <= 0) {
			return ApiResult.error(500, "开通账号数量和开通年限必须大于等于1");
		}
		
		MemberEntity sessionMember = memberService.queryObjectByOpenid(openid);
		if(sessionMember != null && BindStatusEnum.BIND.getCode().equals(sessionMember.getBindStatus())) {
			return ApiResult.error(500, "您已开通，不能重复开通");
		}
		
		String codeCache = SystemCache.smsCodeCache.get(mobile);
		if (!StringUtils.equals(code, codeCache)) {
			return ApiResult.error(500, "验证码错误或失效");
		}

		Date now = new Date();
//		Date endTime = DateUtil.offset(now, DateField.YEAR, time);//到期时间
		Date endTime = DateUtil.offset(now, DateField.DAY_OF_YEAR, time * 365);//一年按365天
		String body = SystemConstant.APP_NAME + "新购";
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setCtime(now);
		orderEntity.setOrderSn(ProjectUtils.genOrderNum("N"));
		orderEntity.setPlatform("weixin");
		orderEntity.setStatus("non");//未支付
		orderEntity.setType(OrderTypeEnum.NEWBUY.getCode());
		orderEntity.setWeixinOpenid(openid);
		
		//计算价格
		int betweenDay = (int) DateUtil.between(now, endTime, DateUnit.DAY);//使用总天数
		if(betweenDay == 0) betweenDay = 1;
		String vipUnitPrice = sysConfigService.getValue(SystemConstant.KEY_VIP_UNIT_PRICE, "1");
		int payMoney = betweenDay * Integer.valueOf(vipUnitPrice) * quantity;
		orderEntity.setPayMoney(new BigDecimal(payMoney));
		
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setFirstStartTime(now);
		orderDetailDto.setStartTime(now);
		orderDetailDto.setEndTime(endTime);
		orderDetailDto.setMobile(mobile);
		orderDetailDto.setQuantity(quantity);
		orderDetailDto.setTime(betweenDay);
		orderEntity.setDetail(JSON.toJSONString(orderDetailDto));
		orderService.save(orderEntity);// save order

		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
		wxPayUnifiedOrderRequest.setOpenid(openid); // 交易类型 JSAPI 必传
		wxPayUnifiedOrderRequest.setBody(body);
		wxPayUnifiedOrderRequest.setOutTradeNo(orderEntity.getOrderSn());
		wxPayUnifiedOrderRequest.setTotalFee(orderEntity.getPayMoney().multiply(new BigDecimal(100)).intValue());
//		wxPayUnifiedOrderRequest.setTotalFee(1);
		wxPayUnifiedOrderRequest.setSpbillCreateIp(ProjectUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
		wxPayUnifiedOrderRequest.setNotifyUrl(paynotifyurl);//支付结果通知回调
		wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);// 交易类型
		// JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付
		WxPayUnifiedOrderResult unifiedOrder = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
		HashMap<String, String> finalpackage = Maps.newHashMap();
		// 时间戳，从 1970 年 1 月 1 日 00:00:00 至今的秒数，即当前的时间
		finalpackage.put("appId", wxPayService.getConfig().getAppId());
		finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		// 随机字符串，长度为32个字符以下
		finalpackage.put("nonceStr", IdUtil.fastSimpleUUID());
		// 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***
		finalpackage.put("package", "prepay_id=" + unifiedOrder.getPrepayId());
		finalpackage.put("signType", "MD5");// 签名算法
		String paySign = SignUtils.createSign(finalpackage, SignType.MD5, wxPayService.getConfig().getMchKey(), new String[0]);
		finalpackage.put("paySign", paySign);// 签名
		finalpackage.put("orderSn", orderEntity.getOrderSn());//自己订单号
		
		// 添加个模板消息
		SubscibeEntity subscibeEntity = new SubscibeEntity();
		subscibeEntity.setCtime(new Date());
		subscibeEntity.setFormid(unifiedOrder.getPrepayId());
		subscibeEntity.setOpenid(openid);
		subscibeEntity.setPlatform(orderEntity.getPlatform());
		subscibeService.save(subscibeEntity);
		
		return ApiResult.ok(finalpackage);
	}
	
	@PostMapping("/upgrade")
	@ApiOperation(value = "升级【增加子账号】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "quantity", value = "开通账号数量", required = true)
	})
	public ApiResult upgrade(@RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
			@ApiIgnore @TokenMember SessionMember sessionMember) throws WxPayException {
		
		if (quantity <= 0) {
			return ApiResult.error(500, "开通账号数量必须大于等于1");
		}
		if (!StringUtils.equals(MemberTypeEnum.VIP.getCode(), sessionMember.getType())) {
			return ApiResult.error(500, "您不是VIP会员，请先开通VIP");
		}
		if(!MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {
			return ApiResult.error(500, "您不是超级管理员，无法升级");
		}
		Date now = new Date();
		if (sessionMember.getEndTime() != null && sessionMember.getEndTime().before(now)) {
			return ApiResult.error(500, "您VIP已到期，请先续期");
		}
		
		String body = SystemConstant.APP_NAME + "升级";
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setCtime(now);
		orderEntity.setMemberId(sessionMember.getMemberId());
		orderEntity.setOrderSn(ProjectUtils.genOrderNum("U"));
		orderEntity.setPlatform("weixin");
		orderEntity.setStatus("non");//未支付
		orderEntity.setType(OrderTypeEnum.UPGRADE.getCode());
		orderEntity.setWeixinOpenid(sessionMember.getOpenid());
		
		//计算价格
		int betweenDay = (int) DateUtil.between(now, sessionMember.getEndTime(), DateUnit.DAY);
		if(betweenDay == 0) betweenDay = 1;
		System.err.println(betweenDay);
		String vipUnitPrice = sysConfigService.getValue(SystemConstant.KEY_VIP_UNIT_PRICE, "1");
		int payMoney = betweenDay * Integer.valueOf(vipUnitPrice) * quantity;
		orderEntity.setPayMoney(new BigDecimal(payMoney));
		
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setFirstStartTime(sessionMember.getStartTime());
		orderDetailDto.setStartTime(now);//当前升级时间
		orderDetailDto.setEndTime(sessionMember.getEndTime());
		orderDetailDto.setMobile(sessionMember.getMobile());
		orderDetailDto.setQuantity(quantity);
		orderDetailDto.setTime(betweenDay);
		orderEntity.setDetail(JSON.toJSONString(orderDetailDto));

		orderService.save(orderEntity);// save order

		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
		wxPayUnifiedOrderRequest.setOpenid(sessionMember.getOpenid()); // 交易类型 JSAPI 必传
		wxPayUnifiedOrderRequest.setBody(body);
		wxPayUnifiedOrderRequest.setOutTradeNo(orderEntity.getOrderSn());
		wxPayUnifiedOrderRequest.setTotalFee(orderEntity.getPayMoney().multiply(new BigDecimal(100)).intValue());
//		wxPayUnifiedOrderRequest.setTotalFee(1);
		wxPayUnifiedOrderRequest.setSpbillCreateIp(ProjectUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
		wxPayUnifiedOrderRequest.setNotifyUrl(paynotifyurl);//支付结果通知回调
		wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);// 交易类型
		// JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付
		WxPayUnifiedOrderResult unifiedOrder = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
		HashMap<String, String> finalpackage = Maps.newHashMap();
		// 时间戳，从 1970 年 1 月 1 日 00:00:00 至今的秒数，即当前的时间
		finalpackage.put("appId", wxPayService.getConfig().getAppId());
		finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		// 随机字符串，长度为32个字符以下
		finalpackage.put("nonceStr", IdUtil.fastSimpleUUID());
		// 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***
		finalpackage.put("package", "prepay_id=" + unifiedOrder.getPrepayId());
		finalpackage.put("signType", "MD5");// 签名算法
		String paySign = SignUtils.createSign(finalpackage, SignType.MD5, wxPayService.getConfig().getMchKey(), new String[0]);
		finalpackage.put("paySign", paySign);// 签名
		finalpackage.put("orderSn", orderEntity.getOrderSn());//自己订单号
		
		// 添加个模板消息
		SubscibeEntity subscibeEntity = new SubscibeEntity();
		subscibeEntity.setCtime(new Date());
		subscibeEntity.setFormid(unifiedOrder.getPrepayId());
		subscibeEntity.setOpenid(sessionMember.getOpenid());
		subscibeEntity.setPlatform(orderEntity.getPlatform());
		subscibeService.save(subscibeEntity);
		
		return ApiResult.ok(finalpackage);
	}
	
	@PostMapping("/renewal")
	@ApiOperation(value = "续费【续期】")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", dataType = "string", name = "time", value = "开通年限（年）", required = true)
	})
	public ApiResult renewal(@RequestParam(value = "time", defaultValue = "1") Integer time,
			@ApiIgnore @TokenMember SessionMember sessionMember) throws WxPayException {
		if (time <= 0) {
			return ApiResult.error(500, "开通年限必须大于等于1");
		}
		if (!StringUtils.equals(MemberTypeEnum.VIP.getCode(), sessionMember.getType())) {
			return ApiResult.error(500, "您不是VIP会员，请先开通VIP");
		}
		if(!MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {
			return ApiResult.error(500, "您不是超级管理员，无法续费");
		}
		Date now = new Date();
		String body = SystemConstant.APP_NAME + "续费";
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setCtime(now);
		orderEntity.setMemberId(sessionMember.getMemberId());
		orderEntity.setOrderSn(ProjectUtils.genOrderNum("R"));
		orderEntity.setPlatform("weixin");
		orderEntity.setStatus("non");//未支付
		orderEntity.setType(OrderTypeEnum.RENEWAL.getCode());
		orderEntity.setWeixinOpenid(sessionMember.getOpenid());
			
//		HashMap<String, Object> params = Maps.newHashMap();
//		params.put("superiorId", sessionMember.getSuperiorId());
//		params.put("bindStatus", BindStatusEnum.BIND.getCode());
//		int bindMemberCount = memberService.queryTotal(params) + 1;//绑定的用户数量，包括自己
		int bindMemberCount = sessionMember.getStaffMaxCount();
		Date startTime = null;
		Date endTime = null;
		int betweenDay;
		if (sessionMember.getEndTime().after(now)) {
			// vip 没过期
//			endTime = DateUtil.offset(sessionMember.getEndTime(), DateField.YEAR, time);
			endTime = DateUtil.offset(sessionMember.getEndTime(), DateField.DAY_OF_YEAR, time * 365);
			startTime = sessionMember.getStartTime();
			betweenDay = (int) DateUtil.between(sessionMember.getEndTime(), endTime, DateUnit.DAY);
		} else {
			// 过期了
//			endTime = DateUtil.offset(now, DateField.YEAR, time);
			endTime = DateUtil.offset(now, DateField.DAY_OF_YEAR, time * 365);
			startTime = now;
			betweenDay = (int) DateUtil.between(startTime, endTime, DateUnit.DAY);
		}
	
		//计算价格
		if(betweenDay == 0) betweenDay = 1;
		String vipUnitPrice = sysConfigService.getValue(SystemConstant.KEY_VIP_UNIT_PRICE, "1");
		int payMoney = betweenDay * Integer.valueOf(vipUnitPrice) * bindMemberCount;
		orderEntity.setPayMoney(new BigDecimal(payMoney));
		
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setFirstStartTime(startTime);
		orderDetailDto.setStartTime(now);
		orderDetailDto.setEndTime(endTime);
		orderDetailDto.setMobile(sessionMember.getMobile());
		orderDetailDto.setQuantity(bindMemberCount);
		orderDetailDto.setTime(betweenDay);
		orderEntity.setDetail(JSON.toJSONString(orderDetailDto));

		orderService.save(orderEntity);// save order

		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
		wxPayUnifiedOrderRequest.setOpenid(sessionMember.getOpenid()); // 交易类型 JSAPI 必传
		wxPayUnifiedOrderRequest.setBody(body);
		wxPayUnifiedOrderRequest.setOutTradeNo(orderEntity.getOrderSn());
		wxPayUnifiedOrderRequest.setTotalFee(orderEntity.getPayMoney().multiply(new BigDecimal(100)).intValue());
//		wxPayUnifiedOrderRequest.setTotalFee(1);
		wxPayUnifiedOrderRequest.setSpbillCreateIp(ProjectUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
		wxPayUnifiedOrderRequest.setNotifyUrl(paynotifyurl);//支付结果通知回调
		wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);// 交易类型
		// JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付
		WxPayUnifiedOrderResult unifiedOrder = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
		HashMap<String, String> finalpackage = Maps.newHashMap();
		// 时间戳，从 1970 年 1 月 1 日 00:00:00 至今的秒数，即当前的时间
		finalpackage.put("appId", wxPayService.getConfig().getAppId());
		finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		// 随机字符串，长度为32个字符以下
		finalpackage.put("nonceStr", IdUtil.fastSimpleUUID());
		// 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=***
		finalpackage.put("package", "prepay_id=" + unifiedOrder.getPrepayId());
		finalpackage.put("signType", "MD5");// 签名算法
		String paySign = SignUtils.createSign(finalpackage, SignType.MD5, wxPayService.getConfig().getMchKey(), new String[0]);
		finalpackage.put("paySign", paySign);// 签名
		finalpackage.put("orderSn", orderEntity.getOrderSn());//自己订单号
		
		// 添加个模板消息
		SubscibeEntity subscibeEntity = new SubscibeEntity();
		subscibeEntity.setCtime(new Date());
		subscibeEntity.setFormid(unifiedOrder.getPrepayId());
		subscibeEntity.setOpenid(sessionMember.getOpenid());
		subscibeEntity.setPlatform(orderEntity.getPlatform());
		subscibeService.save(subscibeEntity);
		
		return ApiResult.ok(finalpackage);
	}
	
	@IgnoreAuth
	@ApiOperation(value = "查询订单结果")
	@PostMapping("/query_order_status")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", dataType = "string", name = "orderSn", value = "订单号", required = true)
	})
	public ApiResult queryOrderStatus(@RequestParam String orderSn) {
		OrderEntity orderEntity = orderService.queryObjectByOrderSn(orderSn);
		Assert.isNullApi(orderEntity, "该订单不存在");
		return ApiResult.ok(orderEntity.getStatus());
	}

	@ApiIgnore
	@IgnoreAuth
	@ApiOperation(value = "支付回调通知处理")
	@PostMapping("/notify")
	public String parseOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
		final WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
		log.info("支付回调通知处理 ==> {}", notifyResult);
		// 根据自己业务场景需要构造返回对象
		if (notifyResult.getReturnCode().equals("SUCCESS")) {
			if (notifyResult.getResultCode().equals("SUCCESS")) {
				String outTradeNo = notifyResult.getOutTradeNo();// 商户订单号
				Integer totalFee = notifyResult.getTotalFee();// 订单总金额，单位为分
				orderService.handleFinish(outTradeNo, totalFee);// 处理订单
				return WxPayNotifyResponse.success("OK");
			}
		}
		return WxPayNotifyResponse.fail("ERROR");
	}

}
