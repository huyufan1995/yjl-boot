package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApplyEntityVo;
import io.renren.api.vo.ApplyVo;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.entity.ApplyRecordEntity;

/**
 * 报名接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
public interface ApplyService {

	ApplyEntity queryObject(Integer id);

	List<ApplyEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(ApplyEntity apply);

	void update(ApplyEntity apply);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	ApplyVo editApply(ApplyEntity applyEntity, SessionMember sessionMember);

	void joinApply(ApplyRecordEntity applyRecordEntity);

	List<ApplyEntity> queryListCreationRecord(Map<String, Object> map);

	List<ApplyEntityVo> queryListVo(Map<String, Object> map);

	int querySumViewCount(Map<String, Object> map);

	List<ApplyEntity> queryListShareRecord(Map<String, Object> map);

	void logicDelete(Integer applyId);

}
