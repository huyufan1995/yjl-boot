package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ApplyRecordEntityVo;
import io.renren.api.vo.ApplyRecordVo;
import io.renren.cms.entity.ApplyRecordEntity;

/**
 * 报名记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
public interface ApplyRecordService {

	ApplyRecordEntity queryObject(Integer id);

	List<ApplyRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(ApplyRecordEntity applyRecord);

	void update(ApplyRecordEntity applyRecord);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelByApplyId(Integer applyId);

	int logicDelBatch(List<Integer> ids);

	ApplyRecordEntity queryObjectByApplyIdAndJoinOpenid(Integer applyId, String joinOpenid);

	List<ApplyRecordVo> queryListByJoinOpenid(Map<String, Object> map);

	List<ApplyRecordEntity> queryListByApplyId(Map<String, Object> map);

	List<ApplyRecordEntityVo> queryListVo(Map<String, Object> map);

	List<ApplyRecordEntity> applyRecordListByApplyId(String id);
}
