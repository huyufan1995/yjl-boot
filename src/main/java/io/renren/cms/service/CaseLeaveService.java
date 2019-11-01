package io.renren.cms.service;

import io.renren.api.vo.CaseLeaveEntityVo;
import io.renren.cms.entity.CaseLeaveEntity;
import io.renren.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * 留言-案例库接口
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:11
 */
public interface CaseLeaveService {
	
	CaseLeaveEntity queryObject(Integer id);
	
	List<CaseLeaveEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CaseLeaveEntity caseLeave);
	
	void update(CaseLeaveEntity caseLeave);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	int logicDel(Integer id);
	
	int logicDelBatch(List<Integer> ids);

    List<CaseLeaveEntityVo> queryListVo(Map<String, Object> map);
}
