package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.OrderEntityVo;
import io.renren.cms.entity.OrderEntity;
import io.renren.dao.BaseDao;

/**
 * 订单
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface OrderDao extends BaseDao<OrderEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	OrderEntity queryObjectByOrderSn(String orderSn);

    List<OrderEntityVo> queryListVo(Map<String, Object> map);
}
