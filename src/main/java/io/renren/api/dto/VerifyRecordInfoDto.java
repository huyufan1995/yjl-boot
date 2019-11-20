package io.renren.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName VerifyRecordInfoDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/20 11:06
 **/
@Data
public class VerifyRecordInfoDto {

    private String portrait;

    private String nickName;

    private String showVip;

    private Date ctime;

    private String verifyTimeStr;
}
