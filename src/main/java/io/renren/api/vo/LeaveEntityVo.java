package io.renren.api.vo;

import io.renren.cms.entity.LeaveEntity;
import lombok.Data;

@Data
public class LeaveEntityVo extends LeaveEntity {

    /**
     *  被留言对象
     */
    private String toName;

    /**
     * 被留言对象公司
     */
    private String toCompany;

}
