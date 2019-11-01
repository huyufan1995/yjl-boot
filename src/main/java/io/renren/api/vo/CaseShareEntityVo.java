package io.renren.api.vo;

import io.renren.cms.entity.CaseShareEntity;
import lombok.Data;

@Data
public class CaseShareEntityVo extends CaseShareEntity {

    /**
     * 分享人
     */
    private String shareName;

    /**
     * 案例标题
     */
    private String caseTitle;
}
