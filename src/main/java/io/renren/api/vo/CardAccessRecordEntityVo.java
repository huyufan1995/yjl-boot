package io.renren.api.vo;

import io.renren.cms.entity.CardAccessRecordEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 名片访问记录
 *
 */
@Data
public class CardAccessRecordEntityVo extends CardAccessRecordEntity {

	/**
	 * 受访人
	 */
	private String intervieweeName;

	/**
	 * 访问人电话
	 */
	private String mobile;
}
