package io.renren.cms.vo;

import io.renren.cms.entity.CommentEntity;
import lombok.Data;

/**
 * @ClassName CommentEntityVo
 * @Description TODO
 * @Author moran
 * @Date 2019/11/25 14:08
 **/
@Data
public class CommentEntityVo extends CommentEntity {

    /**
     * 资讯标题
     */
    private String informationTitle;
    /**
     * 评论人
     */
    private String nickName;
}
