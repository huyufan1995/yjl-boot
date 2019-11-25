package io.renren.cms.vo;

import io.renren.cms.entity.LikeEntity;
import lombok.Data;

/**
 * @ClassName LikeEntityVO
 * @Description TODO
 * @Author moran
 * @Date 2019/11/25 14:35
 **/
@Data
public class LikeEntityVO extends LikeEntity {

    /**
     * 点赞人
     */
    private String nickName;

    private String title;

    private String titleType;
}
