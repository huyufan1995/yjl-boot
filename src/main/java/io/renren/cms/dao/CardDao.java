package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardEntityVo;
import io.renren.cms.entity.CardEntity;
import io.renren.dao.BaseDao;

/**
 * 名片
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface CardDao extends BaseDao<CardEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CardEntity queryObjectByOpenid(String openid);

    List<CardEntityVo> queryListVo(Map<String, Object> map);
}
