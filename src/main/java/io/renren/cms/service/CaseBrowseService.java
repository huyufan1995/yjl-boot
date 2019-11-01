package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.CaseBrowseEntity;
import io.renren.utils.Query;

/**
 * 案例库浏览记录接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
public interface CaseBrowseService {

	CaseBrowseEntity queryObject(Integer id);

	List<CaseBrowseEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(CaseBrowseEntity caseBrowse);

	void update(CaseBrowseEntity caseBrowse);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseBrowseEntity queryObjectByCaseIdAndBrowseOpenidAndShareMemberId(Integer caseId, String browseOpenid,
			Integer shareMemberId);

	List<CaseBrowseEntity> queryListVo(Map<String, Object> map);
}
