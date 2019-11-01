package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.api.vo.ClienteleTagEntityVo;
import io.renren.cms.dao.ClienteleTagDao;
import io.renren.cms.entity.ClienteleTagEntity;
import io.renren.cms.service.ClienteleTagService;

/**
 * 客户标签服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("clienteleTagService")
public class ClienteleTagServiceImpl implements ClienteleTagService {

	@Autowired
	private ClienteleTagDao clienteleTagDao;

	@Override
	public ClienteleTagEntity queryObject(Integer id) {
		return clienteleTagDao.queryObject(id);
	}

	@Override
	public List<ClienteleTagEntity> queryList(Map<String, Object> map) {
		return clienteleTagDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return clienteleTagDao.queryTotal(map);
	}

	@Override
	public void save(ClienteleTagEntity clienteleTag) {
		clienteleTagDao.save(clienteleTag);
	}

	@Override
	public void update(ClienteleTagEntity clienteleTag) {
		clienteleTagDao.update(clienteleTag);
	}

	@Override
	public void delete(Integer id) {
		clienteleTagDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		clienteleTagDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return clienteleTagDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return clienteleTagDao.logicDelBatch(ids);
	}

	@Override
	public int deleteByClienteleIdAndMemberId(Integer clienteleId, Integer memberId) {
		return clienteleTagDao.deleteByClienteleIdAndMemberId(clienteleId, memberId);
	}

	@Override
	public int deleteByClienteleIdAndMemberIdAndTagId(Integer clienteleId, Integer memberId, Integer tagId) {
		return clienteleTagDao.deleteByClienteleIdAndMemberIdAndTagId(clienteleId, memberId, tagId);
	}

	@Override
	public List<ClienteleTagEntityVo> queryListVo(Map<String, Object> map) {
		return clienteleTagDao.queryListVo(map);
	}

	@Override
	public int deleteByTagId(Integer tagId) {
		return clienteleTagDao.deleteByTagId(tagId);
	}

	@Override
	public int deleteByClienteleIdAndTagId(Integer clienteleId, Integer tagId) {
		return clienteleTagDao.deleteByClienteleIdAndTagId(clienteleId, tagId);
	}

}
