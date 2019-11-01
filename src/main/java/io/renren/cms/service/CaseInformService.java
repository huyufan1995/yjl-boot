package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseInformEntityVo;
import io.renren.cms.entity.CaseInformEntity;
import io.renren.utils.Query;

/**
 * 案例-举报接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-10 14:58:35
 */
public interface CaseInformService {

	CaseInformEntity queryObject(Integer id);

	List<CaseInformEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CaseInformEntity caseInform);

	void update(CaseInformEntity caseInform);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseInformEntity queryObjectByOpenidAndCaseId(String openid, Integer caseId);

	List<CaseInformEntityVo> queryListVo(Map<String, Object> map);
}
