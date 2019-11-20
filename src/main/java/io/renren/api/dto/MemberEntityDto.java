package io.renren.api.dto;

import io.renren.cms.entity.MemberEntity;
import lombok.Data;

/**
 * @ClassName MemberEntityDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/18 16:59
 **/
@Data
public class MemberEntityDto extends MemberEntity {

    private Boolean isCollect;

    /**
     * 留言数量
     */
    private Integer leaveTotal;


    /**
     * 是否点赞会员
     */
    private Boolean isLike;

}
