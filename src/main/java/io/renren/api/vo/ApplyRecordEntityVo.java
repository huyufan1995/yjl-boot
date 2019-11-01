package io.renren.api.vo;

import io.renren.cms.entity.ApplyRecordEntity;
import lombok.Data;

@Data
public class ApplyRecordEntityVo extends ApplyRecordEntity {

    /**
     * 分享人姓名
     */
    private String shareName;

    /**
     * 超级管理员姓名
     */
    private String superiorName;

    /**
     * 创建人姓名
     */
    private String memberName;


}
