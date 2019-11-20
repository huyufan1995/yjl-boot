package io.renren.api.dto;

import io.renren.cms.entity.InformationsEntity;
import lombok.Data;

/**
 * 资讯详情DTO
 */
@Data
public class InformationsEntityInfoDto extends InformationsEntity {

    private String createPeople = "越柬寮世联会";

    private String createPeopleLogo ="https://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/yjl/app/e2f74708658d8dbc2a3be0b701f1e56.png";

    /**
     * 浏览量
     */
    private Integer browsTotal;

    /**
     * 是否分享
     */
    private Boolean isShare;

    /**
     * 是否点赞
     */
    private Boolean isLike;


    /**
     * 评论数量
     */
    private Integer commentTotal;

    /**
     * 当前人是否收藏
     */
    private Boolean isCollect;
/*
    *//**
     * 点赞量
     *//*
    private Integer likeTotal;*/


    /**
     * 资讯详情描述
     */
    private String informationDesc;
}
