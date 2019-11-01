package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CardAccessRecordEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 名片访问记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
public interface CardAccessRecordDao extends BaseDao<CardAccessRecordEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CardAccessRecordEntity queryObjectByOpenidAndCardId(@Param("openid") String openid,
			@Param("cardId") Integer cardId);

    List<CardAccessRecordEntityVo> queryListVo(Map<String, Object> map);
}
