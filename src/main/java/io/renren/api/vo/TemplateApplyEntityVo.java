package io.renren.api.vo;

import io.renren.cms.entity.TemplateApplyEntity;
import lombok.Data;


import io.renren.cms.entity.TemplateApplyEntity;
@Data
public class TemplateApplyEntityVo extends TemplateApplyEntity {

    /**
     * 背景图片
     */
    private String bg;

    /**
     * 按钮图片
     */
    private String btn;
}
