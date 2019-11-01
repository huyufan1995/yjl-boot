package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardHolderEntityVo;
import io.renren.cms.entity.CardHolderEntity;
import io.renren.dao.BaseDao;

/**
 * 名片夹
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 17:23:12
 */
public interface CardHolderDao extends BaseDao<CardHolderEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<CardHolderEntityVo> queryListVo(Map<String, Object> map);
}
