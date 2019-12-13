package io.renren.utils.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ApplyRecordPOI
 * @Description TODO
 * @Author moran
 * @Date 2019/12/5 14:50
 **/
@Data
public class ApplyRecordPOI{


    /**
     * 活动标题
     */
    @Excel(name = "活动标题",width = 30, isImportField = "true_st")
    private String applyTitle;
    /**
     * 报名时间
     */
    @Excel(name = "报名时间",width = 30, isImportField = "true_st",databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd")
    private Date ctime;
    /**
     * 报名人姓名
     */
    @Excel(name = "报名人姓名",width = 30, isImportField = "true_st")
    private String nickName;



}
