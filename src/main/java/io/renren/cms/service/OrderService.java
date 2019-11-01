package io.renren.cms.service;

import io.renren.api.vo.OrderEntityVo;
import io.renren.cms.entity.OrderEntity;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 订单接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface OrderService {

	OrderEntity queryObject(Integer id);

	List<OrderEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(OrderEntity order);

	void update(OrderEntity order);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	void handleFinish(String outTradeNo, Integer totalFee);

	OrderEntity queryObjectByOrderSn(String orderSn);

    List<OrderEntityVo> queryListVo(Map<String, Object> map);
}
