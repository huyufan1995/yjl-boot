package io.renren.api.dto;

import io.renren.cms.entity.ApplyRecordEntity;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ApplyRecordEntiyDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/13 11:28
 **/
@Data
public class ApplyRecordEntiyDto extends ApplyRecordEntity {

    private String portrait;

    private String nickName;

    private String showVip;

    private Date joinDate;

}
