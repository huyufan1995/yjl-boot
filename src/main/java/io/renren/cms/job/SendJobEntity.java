package io.renren.cms.job;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName SendJobEntity
 * @Description TODO
 * @Author moran
 * @Date 2019/11/28 10:08
 **/
@Data
public class SendJobEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date startTime;

    private String openid;

    private String applyTitle;

}
