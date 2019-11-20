package io.renren.api.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName VerifyApplyDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/19 17:09
 **/
@Data
public class VerifyApplyDto {

    private String id;

    private String applyTitle;

    private Date endTime;

    private String key;

    private String endTimeStr;

    private List<String> portrait;
}
