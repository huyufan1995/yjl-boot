package io.renren.api.vo;

import io.renren.cms.entity.CaseLeaveEntity;
import lombok.Data;

@Data
public class CaseLeaveEntityVo extends CaseLeaveEntity {

    /**
     * 分享人名称
     */
    private String shareName;
}
