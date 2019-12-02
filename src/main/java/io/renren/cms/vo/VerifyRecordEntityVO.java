package io.renren.cms.vo;

import io.renren.cms.entity.VerifyRecordEntity;
import lombok.Data;

/**
 * @ClassName VerifyRecordEntityVO
 * @Description TODO
 * @Author moran
 * @Date 2019/12/2 15:10
 **/
@Data
public class VerifyRecordEntityVO extends VerifyRecordEntity {

    /**
     * 核销员姓名
     */
    private String verifyName;

    /**
     * 参会人员姓名
     */
    private String memberName;

    /**
     * 活动标题
     */
    private String applyTitle;
}
