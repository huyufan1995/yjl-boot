package io.renren.api.vo;

import io.renren.cms.entity.CaseInformEntity;
import lombok.Data;

@Data
public class CaseInformEntityVo extends CaseInformEntity {

    /**
     * 举报人姓名
     */
    private String informerName;

    /**
     * 案例标题
     */
    private String caseTitle;
}
