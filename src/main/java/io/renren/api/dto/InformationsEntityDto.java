package io.renren.api.dto;

import io.renren.cms.entity.BannerEntity;
import io.renren.cms.entity.InformationsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
/**
 * 资讯Dto
 */
public class InformationsEntityDto extends InformationsEntity {

/*    *//**
     * 评论数量
     *//*
    @ApiModelProperty(value = "评论数量")
    private Integer commentTotal;*/

    /**
     * 评论人头像
     */
    @ApiModelProperty(value = "评论人头像")
    private List<String> portrait;

    private BannerEntity bannerEntity;
}
