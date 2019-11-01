package io.renren.cms.service;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.DeptEntityVo;
import io.renren.cms.entity.DeptEntity;
import io.renren.utils.Query;

/**
 * 部门接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-22 12:18:49
 */
public interface DeptService {

	DeptEntity queryObject(Integer id);

	List<DeptEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	void save(DeptEntity dept);

	void update(DeptEntity dept);

	void delete(Integer id);

	void deleteBatch(Integer[] ids);

	int logicDel(Integer id);

	int logicDelBatch(List<Integer> ids);

	void deleteFull(Integer id);

	void changeDept(String[] memberIds, Integer deptId);

	void join(String openid, Integer deptId);

    List<DeptEntityVo> queryListVo(Map<String, Object> map);
}
