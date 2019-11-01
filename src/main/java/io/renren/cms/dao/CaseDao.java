package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseEntityVo;
import io.renren.api.vo.CaseVo;
import io.renren.cms.entity.CaseEntity;
import io.renren.dao.BaseDao;

/**
 * 案例
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
public interface CaseDao extends BaseDao<CaseEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<CaseVo> queryListByMemberIdOrSuperiorId(Map<String, Object> map);

	List<CaseVo> queryListMyShare(Map<String, Object> map);

    List<CaseEntityVo> queryListVo(Map<String, Object> map);
}
