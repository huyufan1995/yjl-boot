package io.renren.api.vo;

import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.cms.entity.CardHolderEntity;
import lombok.Data;

/**
 * 名片夹
 *
 */
@Data
public class CardHolderEntityVo extends CardHolderEntity {

	/**
	 * 谁的名片夹
	 */
	private String fromName;

}
