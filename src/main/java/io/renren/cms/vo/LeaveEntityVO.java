package io.renren.cms.vo;

import io.renren.cms.entity.LeaveEntity;
import lombok.Data;

/**
 * @ClassName LeaveEntityVO
 * @Description TODO
 * @Author moran
 * @Date 2019/11/27 10:44
 **/
@Data
public class LeaveEntityVO extends LeaveEntity {

    private String leaveName;

    private String memberName;
}
