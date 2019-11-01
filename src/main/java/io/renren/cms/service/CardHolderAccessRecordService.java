package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.CardHolderAccessRecordEntity;

/**
 * 名片夹访问记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-11 10:50:49
 */
public interface CardHolderAccessRecordService {

	CardHolderAccessRecordEntity queryObject(Integer id);

	List<CardHolderAccessRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CardHolderAccessRecordEntity cardHolderAccessRecord);

	void update(CardHolderAccessRecordEntity cardHolderAccessRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CardHolderAccessRecordEntity queryObjectByOpenidAndCardHolderId(Integer cardHolderId, String openid);
}
