package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptEntityVo;
import io.renren.cms.entity.DeptEntity;
import io.renren.dao.BaseDao;

/**
 * 部门
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
public interface DeptDao extends BaseDao<DeptEntity> {
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<DeptEntityVo> queryListVo(Map<String, Object> map);
}
