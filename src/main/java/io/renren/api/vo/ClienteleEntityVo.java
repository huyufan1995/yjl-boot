package io.renren.api.vo;

import io.renren.cms.entity.ClienteleEntity;
import lombok.Data;

@Data
public class ClienteleEntityVo extends ClienteleEntity {

    //负责人名称
    private String functionaryName;

    //备注数量
    private Integer tcrTotal;

    //标签数量
    private Integer tclTotal;

}
