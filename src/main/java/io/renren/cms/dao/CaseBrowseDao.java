package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.cms.entity.CaseBrowseEntity;
import io.renren.dao.BaseDao;

/**
 * 案例库浏览记录
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
public interface CaseBrowseDao extends BaseDao<CaseBrowseEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	CaseBrowseEntity queryObjectByCaseIdAndBrowseOpenidAndShareMemberId(@Param("caseId") Integer caseId,
			@Param("browseOpenid") String browseOpenid, @Param("shareMemberId") Integer shareMemberId);

    List<CaseBrowseEntity> queryListVo(Map<String, Object> map);
}
