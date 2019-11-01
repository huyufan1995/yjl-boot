package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseEntityVo;
import io.renren.api.vo.CaseVo;
import io.renren.cms.entity.CaseEntity;
import io.renren.utils.Query;

/**
 * 案例接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
public interface CaseService {

	CaseEntity queryObject(Integer id);

	List<CaseEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CaseEntity caseEntity);

	void update(CaseEntity caseEntity);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseVo caseInfo(Integer caseId, String openid, Integer shareMemberId);

	List<CaseVo> queryListByMemberIdOrSuperiorId(Map<String, Object> map);

	List<CaseVo> queryListMyShare(Map<String, Object> map);

	List<CaseEntityVo> queryListVo(Map<String, Object> map);
}
