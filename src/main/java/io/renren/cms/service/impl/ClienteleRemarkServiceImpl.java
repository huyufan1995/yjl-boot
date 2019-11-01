package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.api.vo.ClienteleRemarkEntityVo;
import io.renren.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.cms.dao.ClienteleRemarkDao;
import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.cms.service.ClienteleRemarkService;

/**
 * 客户备注服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("clienteleRemarkService")
public class ClienteleRemarkServiceImpl implements ClienteleRemarkService {

	@Autowired
	private ClienteleRemarkDao clienteleRemarkDao;

	@Override
	public ClienteleRemarkEntity queryObject(Integer id) {
		return clienteleRemarkDao.queryObject(id);
	}

	@Override
	public List<ClienteleRemarkEntity> queryList(Map<String, Object> map) {
		return clienteleRemarkDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return clienteleRemarkDao.queryTotal(map);
	}

	@Override
	public void save(ClienteleRemarkEntity clienteleRemark) {
		clienteleRemarkDao.save(clienteleRemark);
	}

	@Override
	public void update(ClienteleRemarkEntity clienteleRemark) {
		clienteleRemarkDao.update(clienteleRemark);
	}

	@Override
	public void delete(Integer id) {
		clienteleRemarkDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		clienteleRemarkDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return clienteleRemarkDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return clienteleRemarkDao.logicDelBatch(ids);
	}

	@Override
	public List<ClienteleRemarkEntity> queryListByClienteleId(Integer clienteleId) {
		return clienteleRemarkDao.queryListByClienteleId(clienteleId);
	}

	@Override
	public List<ClienteleRemarkEntityVo> queryListVo(Map<String, Object> map) {
		return clienteleRemarkDao.queryListVo(map);
	}

}
