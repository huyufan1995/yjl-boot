package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ApplyRecordEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.api.vo.ApplyRecordVo;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.dao.BaseDao;

/**
 * 报名记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
public interface ApplyRecordDao extends BaseDao<ApplyRecordEntity> {

	int logicDel(Integer id);

	int logicDelByApplyId(Integer applyId);

	int logicDelBatch(List<Integer> ids);

	ApplyRecordEntity queryObjectByApplyIdAndJoinOpenid(@Param("applyId") Integer applyId,
			@Param("joinOpenid") String joinOpenid);

	List<ApplyRecordVo> queryListByJoinOpenid(Map<String, Object> map);

	List<ApplyRecordEntity> queryListByApplyId(Map<String, Object> map);

	List<ApplyRecordEntityVo> queryListVo(Map<String, Object> map);

	List<ApplyRecordEntity> applyRecordListByApplyId(String id);
}
