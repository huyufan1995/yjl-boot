package io.renren.api.dto;

import io.renren.cms.entity.LeaveEntity;
import lombok.Data;

/**
 * @ClassName LeaveEntityDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/18 15:54
 **/
@Data
public class LeaveEntityDto extends LeaveEntity {

    /**
     * 点赞数量
     */
    private String likeTotal;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 会员头像
     */
    private String portrait;

    /**
     * 会员类型
     */
    private String type;

    /**
     * 是否显示vip logo
     */
    private String showVip;


    /**
     * 是否点赞
     */
    private Boolean isLike;

    private String content;

}
