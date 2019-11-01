package io.renren.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.CardHolderAccessRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 名片夹访问记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-11 10:50:49
 */
public interface CardHolderAccessRecordDao extends BaseDao<CardHolderAccessRecordEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CardHolderAccessRecordEntity queryObjectByOpenidAndCardHolderId(@Param("cardHolderId") Integer cardHolderId,
			@Param("openid") String openid);
}
