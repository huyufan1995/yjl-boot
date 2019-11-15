package io.renren.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ApplyReviewEntityDto
 * @Description TODO
 * @Author moran
 * @Date 2019/11/14 11:27
 **/
@Data
public class ApplyReviewEntityDto implements Serializable{
        private static final long serialVersionUID = 1L;
        /** 活动回顾 */
        private String applyReviewContent;
        /** 1:text文本 2：image图片 3video视频 */
        private String applyReviewType;
        /** 视频链接 */
        private String reviewVideoLink;

    }
