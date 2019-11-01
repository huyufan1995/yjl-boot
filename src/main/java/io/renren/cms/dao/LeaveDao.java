package io.renren.cms.dao;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.LeaveEntityVo;
import io.renren.cms.entity.LeaveEntity;
import io.renren.dao.BaseDao;

/**
 * 留言
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface LeaveDao extends BaseDao<LeaveEntity> {

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<LeaveEntity> searchList(Map<String, Object> params);

    List<LeaveEntityVo> queryListVo(Map<String, Object> map);
}
