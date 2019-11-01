package io.renren.api.vo;

import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.MemberEntity;
import lombok.Data;

@Data
public class CardEntityVo extends CardEntity {

    private Integer cardTotal;
}
