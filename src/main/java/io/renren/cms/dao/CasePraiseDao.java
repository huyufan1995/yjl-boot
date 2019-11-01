package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CasePraiseEntityVo;
import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.CasePraiseEntity;
import io.renren.dao.BaseDao;

/**
 * 案例-赞
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
public interface CasePraiseDao extends BaseDao<CasePraiseEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CasePraiseEntity queryObjectByOpenidAndCaseId(@Param("openid") String openid, @Param("caseId") Integer caseId);

    List<CasePraiseEntityVo> queryListVo(Map<String, Object> map);

}
