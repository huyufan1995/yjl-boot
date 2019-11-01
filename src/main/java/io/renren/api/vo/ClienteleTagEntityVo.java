package io.renren.api.vo;

import io.renren.cms.entity.ClienteleTagEntity;
import lombok.Data;

@Data
public class ClienteleTagEntityVo extends ClienteleTagEntity {
    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建人手机号
     */
    private String createMobile;

    /**
     * 创建人公司
     */
    private String createCompany;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 客户姓名
     */
    private String clienteleName;
}
