package io.renren.cms.service.impl;

import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.InformationsEntityDto;
import io.renren.cms.dao.InformationDao;
import io.renren.cms.entity.InformationsEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.cms.service.InformationService;

/**
 * 服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("informationService")
public class InformationServiceImpl implements InformationService {

	@Autowired
	private InformationDao informationDao;
	
	@Override
	public InformationsEntity queryObject(Integer id){
		return informationDao.queryObject(id);
	}
	
	@Override
	public List<InformationsEntity> queryList(Map<String, Object> map){
		return informationDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return informationDao.queryTotal(map);
	}
	
	@Override
	public void save(InformationsEntity information){
		information.setIsDel(SystemConstant.F_STR);
		information.setCtime(new Date());
		information.setUpdateTime(new Date());
		information.setAuditStatus(AuditStatusEnum.UNCOMMIT.getCode());
		informationDao.save(information);
	}
	
	@Override
	public void update(InformationsEntity information){
		informationDao.update(information);
	}
	
	@Override
	public void delete(Integer id){
		informationDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		informationDao.deleteBatch(ids);
	}
	
	@Override
	public int logicDel(Integer id) {
		return informationDao.logicDel(id);
	}
	
	@Override
	public int logicDelBatch(List<Integer> ids) {
		return informationDao.logicDelBatch(ids);
	}

	@Override
	public int release(Integer id) {
		InformationsEntity entity = new InformationsEntity();
		entity.setId(id);
		entity.setShowStatus(SystemConstant.T_STR);
		entity.setAuditMsg("通过");
		entity.setAuditStatus(AuditStatusEnum.PASS.getCode());
		return informationDao.update(entity);
	}

	@Override
	public int commit(Integer id) {
		InformationsEntity entity = new InformationsEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
		return informationDao.update(entity);
	}

	@Override
	public int revocation(Integer id) {
		InformationsEntity entity = new InformationsEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.UNCOMMIT.getCode());
		return informationDao.update(entity);
	}

	@Override
	public int rejectInformation(Integer id) {
		InformationsEntity entity = new InformationsEntity();
		entity.setId(id);
		entity.setAuditStatus(AuditStatusEnum.REJECT.getCode());
		return informationDao.update(entity);
	}

	@Override
	public List<InformationsEntityDto> queryListDto(Map<String, Object> map) {
		return informationDao.queryListDto(map);
	}


}
