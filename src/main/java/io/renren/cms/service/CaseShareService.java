package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseShareEntityVo;
import io.renren.cms.entity.CaseShareEntity;
import io.renren.utils.Query;

/**
 * 案例-分享接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
public interface CaseShareService {

	CaseShareEntity queryObject(Integer id);

	List<CaseShareEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CaseShareEntity caseShare);

	void update(CaseShareEntity caseShare);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseShareEntity queryObjectByCaseIdAndShareMemberId(Integer caseId, Integer shareMemberId);

	List<CaseShareEntityVo> queryListVo(Map<String, Object> map);
}
