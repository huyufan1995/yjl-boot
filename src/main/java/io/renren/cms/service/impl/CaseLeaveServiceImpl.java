package io.renren.cms.service.impl;

import io.renren.api.vo.CaseLeaveEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.CaseLeaveDao;
import io.renren.cms.entity.CaseLeaveEntity;
import io.renren.cms.service.CaseLeaveService;

/**
 * 留言-案例库服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("caseLeaveService")
public class CaseLeaveServiceImpl implements CaseLeaveService {

	@Autowired
	private CaseLeaveDao caseLeaveDao;
	
	@Override
	public CaseLeaveEntity queryObject(Integer id){
		return caseLeaveDao.queryObject(id);
	}
	
	@Override
	public List<CaseLeaveEntity> queryList(Map<String, Object> map){
		return caseLeaveDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return caseLeaveDao.queryTotal(map);
	}
	
	@Override
	public void save(CaseLeaveEntity caseLeave){
		caseLeaveDao.save(caseLeave);
	}
	
	@Override
	public void update(CaseLeaveEntity caseLeave){
		caseLeaveDao.update(caseLeave);
	}
	
	@Override
	public void delete(Integer id){
		caseLeaveDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		caseLeaveDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return caseLeaveDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return caseLeaveDao.logicDelBatch(ids);
	}

	@Override
	public List<CaseLeaveEntityVo> queryListVo(Map<String, Object> map) {
		return caseLeaveDao.queryListVo(map);
	}


}
