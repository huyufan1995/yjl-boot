package io.renren.api.dto;

import io.renren.cms.entity.ApplyEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ApplyEntityDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/13 11:18
 **/
@Data
public class ApplyEntityDto extends ApplyEntity {
    /**
     *浏览头像
     **/
    private List<String> portrait;

    /**
     * 活动状态
     **/
    private String applyStatus;

    /**
     * 活动报名人数量
     */
    private Integer joinTotal;


    /**
     * 活动回顾
     */
    private ApplyReviewEntityDto applyReviewEntityDto;


    /**
     * 是否收藏
     */
    private Boolean isCollect;
}
