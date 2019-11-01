package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ApplyEntityVo;
import io.renren.cms.entity.ApplyEntity;
import io.renren.dao.BaseDao;

/**
 * 报名
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
public interface ApplyDao extends BaseDao<ApplyEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<ApplyEntity> queryListCreationRecord(Map<String, Object> map);

	List<ApplyEntity> queryListShareRecord(Map<String, Object> map);

	List<ApplyEntityVo> queryListVo(Map<String, Object> map);

	int querySumViewCount(Map<String, Object> map);
}
