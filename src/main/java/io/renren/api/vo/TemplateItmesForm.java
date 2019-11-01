package io.renren.api.vo;

import java.util.List;

import io.renren.cms.entity.TemplateItmeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateItmesForm {

	private String openid;

	private List<TemplateItmeEntity> items;

}
