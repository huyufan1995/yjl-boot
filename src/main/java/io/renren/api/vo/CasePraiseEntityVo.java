package io.renren.api.vo;

import io.renren.cms.entity.CasePraiseEntity;
import lombok.Data;

@Data
public class CasePraiseEntityVo extends CasePraiseEntity {

    /**
     * 点赞人
     */
    private String casepraiseName;

    /**
     * 案例标题
     */
    private String caseTitle;
}
