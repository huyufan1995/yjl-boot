package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleAllotRecordEntityVo;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.api.vo.ClienteleAllotRecordVo;
import io.renren.cms.dao.ClienteleAllotRecordDao;
import io.renren.cms.entity.ClienteleAllotRecordEntity;
import io.renren.cms.service.ClienteleAllotRecordService;

/**
 * 客户分片记录服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("clienteleAllotRecordService")
public class ClienteleAllotRecordServiceImpl implements ClienteleAllotRecordService {

	@Autowired
	private ClienteleAllotRecordDao clienteleAllotRecordDao;

	@Override
	public ClienteleAllotRecordEntity queryObject(Integer id) {
		return clienteleAllotRecordDao.queryObject(id);
	}

	@Override
	public List<ClienteleAllotRecordEntity> queryList(Map<String, Object> map) {
		return clienteleAllotRecordDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return clienteleAllotRecordDao.queryTotal(map);
	}

	@Override
	public void save(ClienteleAllotRecordEntity clienteleAllotRecord) {
		clienteleAllotRecordDao.save(clienteleAllotRecord);
	}

	@Override
	public void update(ClienteleAllotRecordEntity clienteleAllotRecord) {
		clienteleAllotRecordDao.update(clienteleAllotRecord);
	}

	@Override
	public void delete(Integer id) {
		clienteleAllotRecordDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		clienteleAllotRecordDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return clienteleAllotRecordDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return clienteleAllotRecordDao.logicDelBatch(ids);
	}

	@Override
	public void insertBatch(List<ClienteleAllotRecordEntity> record) {
		clienteleAllotRecordDao.insertBatch(record);
	}

	@Override
	public List<ClienteleAllotRecordVo> queryListByClienteleId(Integer clienteleId) {
		return clienteleAllotRecordDao.queryListByClienteleId(clienteleId);
	}

	@Override
	public List<ClienteleAllotRecordEntityVo> queryListVo(Map<String, Object> map) {
		return clienteleAllotRecordDao.queryListVo(map);
	}

}
