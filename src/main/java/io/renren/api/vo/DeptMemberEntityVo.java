package io.renren.api.vo;

import io.renren.cms.entity.DeptMemberEntity;
import lombok.Data;

@Data
public class DeptMemberEntityVo extends DeptMemberEntity {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 公司名称
     */
    private String company;
}
