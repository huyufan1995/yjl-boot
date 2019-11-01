package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.LeaveEntityVo;
import io.renren.cms.entity.LeaveEntity;
import io.renren.utils.Query;

/**
 * 留言接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
public interface LeaveService {

	LeaveEntity queryObject(Integer id);

	List<LeaveEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(LeaveEntity leave);

	void update(LeaveEntity leave);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	List<LeaveEntity> searchList(Map<String, Object> params);

    List<LeaveEntityVo> queryListVo(Map<String, Object> map);
}
