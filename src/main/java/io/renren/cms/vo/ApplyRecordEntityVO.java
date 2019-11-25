package io.renren.cms.vo;

import io.renren.cms.entity.ApplyRecordEntity;
import lombok.Data;

/**
 * @ClassName ApplyRecordEntityVO
 * @Description TODO
 * @Author moran
 * @Date 2019/11/23 13:33
 **/
@Data
public class ApplyRecordEntityVO extends ApplyRecordEntity {

    private String applyTitle;

    private String nickName;
}
