package io.renren.api.vo;

import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import lombok.Data;

@Data
public class TemplateWebsiteUseRecordEntityVo extends TemplateWebsiteUseRecordEntity {

    /**
     * 个人分享官网二维码图片
     */
    private String qrcode;
}
