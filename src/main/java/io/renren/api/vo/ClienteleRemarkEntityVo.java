package io.renren.api.vo;

import io.renren.cms.entity.ClienteleRemarkEntity;
import lombok.Data;

@Data
public class ClienteleRemarkEntityVo extends ClienteleRemarkEntity {

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
     * 客户名称
     */
    private String clienteleName;

    /**
     * 客户电话
     */
    private String clienteleMobile;
}
