package io.renren.api.vo;

import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.OrderEntity;
import lombok.Data;

import java.util.Date;

@Data
public class OrderEntityVo extends OrderEntity {
    /**
     * 公司
     */
    private String company;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 会员名称
     */
    private String memberName;

    private Date firstStartTime;
    private Date startTime;
    private Date endTime;
    private int quantity;
    private int time;
}
