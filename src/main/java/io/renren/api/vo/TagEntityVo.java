package io.renren.api.vo;

import io.renren.cms.entity.TagEntity;
import lombok.Data;

@Data
public class TagEntityVo extends TagEntity {

    private String createName;
}
