package io.renren.cms.service;

import io.renren.api.vo.CardHolderEntityVo;
import io.renren.cms.entity.CardHolderEntity;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 名片夹接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 17:23:12
 */
public interface CardHolderService {
	
	CardHolderEntity queryObject(Integer id);
	
	List<CardHolderEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CardHolderEntity cardHolder);
	
	void update(CardHolderEntity cardHolder);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<CardHolderEntityVo> queryListVo(Map<String, Object> map);
}
