package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.CaseLeaveEntityVo;
import io.renren.cms.entity.CaseLeaveEntity;
import io.renren.dao.BaseDao;

/**
 * 留言-案例库
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
public interface CaseLeaveDao extends BaseDao<CaseLeaveEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<CaseLeaveEntityVo> queryListVo(Map<String, Object> map);
}
