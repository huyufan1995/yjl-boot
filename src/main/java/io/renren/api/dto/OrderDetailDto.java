package io.renren.api.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单详情
 * @author yujia
 *
 */
@Getter
@Setter
public class OrderDetailDto {

	private Date firstStartTime;
	private Date startTime;
	private Date endTime;
	private String mobile;
	private int quantity;
	private int time;

}
