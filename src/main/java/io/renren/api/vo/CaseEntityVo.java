package io.renren.api.vo;

import io.renren.cms.entity.CaseEntity;
import lombok.Data;

@Data
public class CaseEntityVo extends CaseEntity {

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 公司
     */
    private String company;

    /**
     * 手机号
     */
    private String mobile;
}
