package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardAccessRecordEntityVo;
import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.utils.Query;

/**
 * 名片访问记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface CardAccessRecordService {

	CardAccessRecordEntity queryObject(Integer id);

	List<CardAccessRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CardAccessRecordEntity cardAccessRecord);

	void update(CardAccessRecordEntity cardAccessRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CardAccessRecordEntity queryObjectByOpenidAndCardId(String openid, Integer cardId);

    List<CardAccessRecordEntityVo> queryListVo(Map<String, Object> map);
}
