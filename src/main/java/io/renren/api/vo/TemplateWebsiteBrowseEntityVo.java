package io.renren.api.vo;

import io.renren.cms.entity.TemplateWebsiteBrowseEntity;
import lombok.Data;

@Data
public class TemplateWebsiteBrowseEntityVo extends TemplateWebsiteBrowseEntity {

    /**
     * 被浏览人名称
     */
    private String forName;

}
