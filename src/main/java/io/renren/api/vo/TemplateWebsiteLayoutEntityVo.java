package io.renren.api.vo;

import io.renren.cms.entity.TemplateWebsiteLayoutEntity;
import lombok.Data;

@Data
public class TemplateWebsiteLayoutEntityVo extends TemplateWebsiteLayoutEntity {

    private String nickName;

    private String mobile;

    private String company;

    private String useTotal;
}
