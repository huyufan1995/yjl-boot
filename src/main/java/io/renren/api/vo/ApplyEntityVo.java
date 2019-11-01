package io.renren.api.vo;

import io.renren.cms.entity.ApplyEntity;
import lombok.Data;

@Data
public class ApplyEntityVo extends ApplyEntity {

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 超级管理员名称
     */
    private String superiorName;
}
