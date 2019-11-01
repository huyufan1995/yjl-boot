package io.renren.cms.service;

import io.renren.api.vo.CardEntityVo;
import io.renren.api.vo.CardVo;
import io.renren.cms.entity.CardEntity;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 名片接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface CardService {

	CardEntity queryObject(Integer id);

	List<CardEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CardEntity card);

	void update(CardEntity card);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CardEntity queryObjectByOpenid(String openid);

	CardVo view(String openid, Integer cardId);

	void modify(CardEntity cardEntity);

	CardVo info(String openid);

	List<CardEntityVo> queryListVo(Map<String, Object> map);
}
