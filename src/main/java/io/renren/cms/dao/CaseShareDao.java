package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseShareEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.CaseShareEntity;
import io.renren.dao.BaseDao;

/**
 * 案例-分享
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
public interface CaseShareDao extends BaseDao<CaseShareEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseShareEntity queryObjectByCaseIdAndShareMemberId(@Param("caseId") Integer caseId,
			@Param("shareMemberId") Integer shareMemberId);

    List<CaseShareEntityVo> queryListVo(Map<String, Object> map);
}
