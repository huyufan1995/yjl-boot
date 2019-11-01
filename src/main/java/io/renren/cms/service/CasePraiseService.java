package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CasePraiseEntityVo;
import io.renren.cms.entity.CasePraiseEntity;
import io.renren.utils.Query;

/**
 * 案例-赞接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
public interface CasePraiseService {

	CasePraiseEntity queryObject(Integer id);

	List<CasePraiseEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CasePraiseEntity casePraise);

	void update(CasePraiseEntity casePraise);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CasePraiseEntity queryObjectByOpenidAndCaseId(String openid, Integer caseId);

    List<CasePraiseEntityVo> queryListVo(Map<String, Object> map);
}
