package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseInformEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.CaseInformEntity;
import io.renren.dao.BaseDao;

/**
 * 案例-举报
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-10 14:58:35
 */
public interface CaseInformDao extends BaseDao<CaseInformEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseInformEntity queryObjectByOpenidAndCaseId(@Param("openid") String openid, @Param("caseId") Integer caseId);

	List<CaseInformEntityVo> queryListVo(Map<String, Object> map);
}
