package io.renren.api.dto;

import io.renren.cms.entity.CommentEntity;
import lombok.Data;

/**
 * 评论
 */
@Data
public class CommentEntityDto extends CommentEntity {

    /**
     * 评论人头像
     */
    private String portrait;

    /**
     * 点赞数量
     */
    private Integer likeTotal;

    /**
     * 评论人名称
     */
    private String nickName;

    /**
     * 是否点赞
     */
    private Boolean isLike;

    /**
     * 是否展示VIP  logo
     */
    private String showVip;

    /**
     * 会员类型  common游客     vip会员
     */
    private String type;
}
