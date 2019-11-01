package io.renren.api.vo;

import io.renren.cms.entity.DeptEntity;
import lombok.Data;

@Data
public class DeptEntityVo extends DeptEntity {

    /**
     * 超级管理员姓名
     */
    private String superiorName;

    /**
     * 公司名称
     */
    private String company;

}
