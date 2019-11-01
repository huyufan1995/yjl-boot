package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.json.JSONUtil;
import io.renren.api.dto.OrderDetailDto;
import io.renren.api.vo.OrderEntityVo;
import io.renren.cms.entity.OrderEntity;
import io.renren.cms.service.OrderService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 订单
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@RestController
@RequestMapping("order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("order:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
		List<OrderEntityVo> orderList = orderService.queryListVo(query);
		orderList.forEach(orderEntityVo ->
		{
			String detail = orderEntityVo.getDetail();
			OrderDetailDto orderDetailDto = JSONUtil.toBean(detail, OrderDetailDto.class);
			BeanUtils.copyProperties(orderDetailDto,orderEntityVo);
		});
		int total = orderService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(orderList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("order:info")
	public R info(@PathVariable("id") Integer id){
		OrderEntity order = orderService.queryObject(id);
		
		return R.ok().put("order", order);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("order:save")
	public R save(@RequestBody OrderEntity order){
		orderService.save(order);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("order:update")
	public R update(@RequestBody OrderEntity order){
		orderService.update(order);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("order:delete")
	public R delete(@RequestBody Integer[] ids){
		orderService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("order:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		orderService.logicDel(id);
		return R.ok();
	}
	
}
