package io.renren.cms.service.impl;

import io.renren.api.dto.LeaveEntityDto;
import io.renren.cms.vo.LeaveEntityVO;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.LeaveDao;
import io.renren.cms.entity.LeaveEntity;
import io.renren.cms.service.LeaveService;

/**
 * 留言服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private LeaveDao leaveDao;
	
	@Override
	public LeaveEntity queryObject(Integer id){
		return leaveDao.queryObject(id);
	}
	
	@Override
	public List<LeaveEntity> queryList(Map<String, Object> map){
		return leaveDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return leaveDao.queryTotal(map);
	}
	
	@Override
	public void save(LeaveEntity leave){
		leaveDao.save(leave);
	}
	
	@Override
	public void update(LeaveEntity leave){
		leaveDao.update(leave);
	}
	
	@Override
	public void delete(Integer id){
		leaveDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		leaveDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return leaveDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return leaveDao.logicDelBatch(ids);
	}

	@Override
	public List<LeaveEntityDto> queryListByMemberId(Map<String, Object> map) {
		return leaveDao.queryListByMemberId(map);
	}

	@Override
	public void updateMsgStatus(Integer memberId) {
		leaveDao.updateMsgStatus(memberId);
	}

	@Override
	public List<LeaveEntityDto> queryListDto(Map<String, Object> map) {
		return leaveDao.queryListDto(map);
	}

	@Override
	public List<LeaveEntityVO> queryListVO(Map<String, Object> map) {
		return leaveDao.queryListVO(map);
	}


}
