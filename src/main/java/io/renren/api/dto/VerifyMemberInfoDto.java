package io.renren.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName VerifyMemberInfoDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/19 13:33
 **/
@Data
public class VerifyMemberInfoDto {

    /**
     * 活动Id
     */
    private String id;
    /** 活动标题 */
    private String applyTitle;
    /** 活动开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    /** 活动结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    /** 创建时间 */
    private Date ctime;
    /** 活动地址 */
    private String applyLocation;
    /** 创建人 */
    private String createPeople;
    /** 活动详情 */
    private String applyContent;
    /**
     * pass:通过reject:不通过pending:审核中，uncommit未提交审核
     */
    private String auditStatus;

    /**
     * t:展示 f:暂停
     */
    private String showStatus;

    private String portrait;

    private String nickName;

    private String showVip;

    private String memberId;

    private String code;

}
