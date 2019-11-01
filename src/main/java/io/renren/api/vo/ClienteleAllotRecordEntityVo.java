package io.renren.api.vo;

import io.renren.cms.entity.ClienteleAllotRecordEntity;
import lombok.Data;

@Data
public class ClienteleAllotRecordEntityVo extends ClienteleAllotRecordEntity {

    /**
     * 客户名称
     */
    private String clienteleName;

    /**
     * 操作人
     */
    private String operationName;

    /**
     * 源负责人
     */
    private String targetName;

    /**
     * 目标负责人
     */
    private String sourceName;
}
