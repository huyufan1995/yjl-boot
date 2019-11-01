package io.renren.cms.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.date.DateUtil;
import io.renren.api.component.TemplateMsgHandler;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.OrderDetailDto;
import io.renren.api.vo.OrderEntityVo;
import io.renren.cms.dao.CardDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.dao.OrderDao;
import io.renren.cms.dao.SubscibeDao;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.OrderEntity;
import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.OrderService;
import io.renren.enums.BindStatusEnum;
import io.renren.enums.CertTypeEnum;
import io.renren.enums.MemberRoleEnum;
import io.renren.enums.MemberTypeEnum;
import io.renren.enums.OrderTypeEnum;
import io.renren.enums.TemplateMsgTypeEnum;
import io.renren.utils.validator.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private CardDao cardDao;
	@Autowired
	private TemplateMsgHandler templateMsgHandler;
	@Autowired
	private SubscibeDao subscibeDao;

	@Override
	public OrderEntity queryObject(Integer id) {
		return orderDao.queryObject(id);
	}

	@Override
	public List<OrderEntity> queryList(Map<String, Object> map) {
		return orderDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return orderDao.queryTotal(map);
	}

	@Override
	public void save(OrderEntity order) {
		orderDao.save(order);
	}

	@Override
	public void update(OrderEntity order) {
		orderDao.update(order);
	}

	@Override
	public void delete(Integer id) {
		orderDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		orderDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return orderDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return orderDao.logicDelBatch(ids);
	}

	/**
	 * 处理支付通知结果订单
	 */
	@Override
	@Transactional
	public void handleFinish(String outTradeNo, Integer totalFee) {
		OrderEntity orderEntity = orderDao.queryObjectByOrderSn(outTradeNo);
		Assert.isNullApi(orderEntity, "该订单不存在");
		if (orderEntity.getPaymentTime() != null || "paid".equals(orderEntity.getStatus())) {
			// 有支付时间说明已经支付
			return;
		}
		// 比对金额
		int payMoney = orderEntity.getPayMoney().multiply(new BigDecimal("100")).intValue();
		if (payMoney != totalFee.intValue()) {
			// 支付金额与通知金额不符
			// throw new ApiException(ResponseCodeEnum.MONEY_DISCREPANCY);
			log.error("支付金额与通知金额不符,付款金额{},真实付款金额{}", payMoney, totalFee);
		}

		//更新支付时间和支付状态
		OrderEntity tempOrder = new OrderEntity();
		tempOrder.setId(orderEntity.getId());
		tempOrder.setStatus("paid");
		tempOrder.setPaymentTime(new Date());
		OrderDetailDto orderDetailDto = JSON.parseObject(orderEntity.getDetail(), OrderDetailDto.class);
		if (OrderTypeEnum.NEWBUY.getCode().equals(orderEntity.getType())) {
			//新购
			CardEntity cardEntity = cardDao.queryObjectByOpenid(orderEntity.getWeixinOpenid());
			MemberEntity bossMember = memberDao.queryObjectByOpenid(orderEntity.getWeixinOpenid());
			if(bossMember == null) {
				bossMember = new MemberEntity();
			}
			bossMember.setAuthStatus(SystemConstant.T_STR)
				.setBindStatus(BindStatusEnum.BIND.getCode())
				.setCertType(CertTypeEnum.UNKNOWN.getCode())
				.setCtime(new Date())
				.setEndTime(orderDetailDto.getEndTime())
				.setGender(cardEntity.getGender())
				.setIsDel(SystemConstant.F_STR)
				.setMobile(orderDetailDto.getMobile())
				.setNickname(cardEntity.getName())
				.setOpenid(orderEntity.getWeixinOpenid())
				.setPortrait(cardEntity.getPortrait())
				.setRealName(cardEntity.getName())
				.setRole(MemberRoleEnum.BOSS.getCode())
				.setStaffMaxCount(orderDetailDto.getQuantity())
				.setStartTime(orderDetailDto.getStartTime())
				.setStatus("normal")
				.setSuperiorId(0)//boss 上级ID 默认为0
				.setCompany(cardEntity.getCompany())
				.setType(MemberTypeEnum.VIP.getCode());
			
			if(bossMember.getId() != null) {
				memberDao.update(bossMember);
			}else {
				memberDao.save(bossMember);
			}
			tempOrder.setMemberId(bossMember.getId());
			
			//开通模板消息
			SubscibeEntity subscibeEntity = subscibeDao.queryObjectByOpenid(orderEntity.getWeixinOpenid());
			if (subscibeEntity != null) {
				templateMsgHandler.sendAsyncDelay(SystemConstant.APP_PAGE_PATH_CENTER, 
						orderEntity.getWeixinOpenid(),
						TemplateMsgTypeEnum.BUYVIP.getTemplateId(),
						subscibeEntity.getFormid(),
						10000L,
						SystemConstant.APP_NAME + "-VIP开通",
						DateUtil.formatDateTime(tempOrder.getPaymentTime()),
						orderEntity.getPayMoney() + "");
			}
			
		} else if (OrderTypeEnum.UPGRADE.getCode().equals(orderEntity.getType())) {
			//升级，增加子账号
			MemberEntity bossMember = memberDao.queryObject(orderEntity.getMemberId());
			Assert.isNullApi(bossMember, "该用户不存在");
			MemberEntity tempMember = new MemberEntity();
			tempMember.setId(bossMember.getId());
			tempMember.setStaffMaxCount(bossMember.getStaffMaxCount() + orderDetailDto.getQuantity());//更新最大员工数量
			memberDao.update(tempMember);
		} else if (OrderTypeEnum.RENEWAL.getCode().equals(orderEntity.getType())) {
			//续费
			MemberEntity bossMember = memberDao.queryObject(orderEntity.getMemberId());
			Assert.isNullApi(bossMember, "该用户不存在");
			MemberEntity tempMember = new MemberEntity();
			tempMember.setId(bossMember.getId());
			tempMember.setStartTime(orderDetailDto.getFirstStartTime());
			tempMember.setEndTime(orderDetailDto.getEndTime());
			memberDao.update(tempMember);//更新自己
			tempMember.setSuperiorId(bossMember.getId());
			memberDao.updateBySuperiorId(tempMember);//同步更新所有下属开始过期时间
			
			//续费模板消息
			SubscibeEntity subscibeEntity = subscibeDao.queryObjectByOpenid(orderEntity.getWeixinOpenid());
			if (subscibeEntity != null) {
				templateMsgHandler.sendAsyncDelay(SystemConstant.APP_PAGE_PATH_CENTER, 
						orderEntity.getWeixinOpenid(),
						TemplateMsgTypeEnum.CONTINUEVIP.getTemplateId(),
						subscibeEntity.getFormid(),
						10000L,
						SystemConstant.APP_NAME + "-VIP续费",
						DateUtil.formatDateTime(orderDetailDto.getEndTime()),
						orderDetailDto.getTime() + "天",
						DateUtil.formatDateTime(tempOrder.getPaymentTime()),
						orderEntity.getPayMoney() + "");
			}
		}
		//更新订单信息
		orderDao.update(tempOrder);
	}

	@Override
	public OrderEntity queryObjectByOrderSn(String orderSn) {
		return orderDao.queryObjectByOrderSn(orderSn);
	}

    @Override
    public List<OrderEntityVo> queryListVo(Map<String, Object> map) {
        return orderDao.queryListVo(map);
    }

}
