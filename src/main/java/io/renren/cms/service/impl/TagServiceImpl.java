package io.renren.cms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import io.renren.api.dto.SessionMember;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.TagEntityVo;
import io.renren.cms.dao.ClienteleDao;
import io.renren.cms.dao.ClienteleTagDao;
import io.renren.cms.dao.TagDao;
import io.renren.cms.entity.ClienteleEntity;
import io.renren.cms.entity.ClienteleTagEntity;
import io.renren.cms.entity.TagEntity;
import io.renren.cms.service.TagService;
import io.renren.utils.validator.Assert;

/**
 * 标签服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("tagService")
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;
	@Autowired
	private ClienteleTagDao clienteleTagDao;
	@Autowired
	private ClienteleDao clienteleDao;

	@Override
	public TagEntity queryObject(Integer id) {
		return tagDao.queryObject(id);
	}

	@Override
	public List<TagEntity> queryList(Map<String, Object> map) {
		return tagDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return tagDao.queryTotal(map);
	}

	@Override
	public void save(TagEntity tag) {
		tagDao.save(tag);
	}

	@Override
	public void update(TagEntity tag) {
		tagDao.update(tag);
	}

	@Override
	public void delete(Integer id) {
		tagDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		tagDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return tagDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return tagDao.logicDelBatch(ids);
	}

	@Override
	public TagEntity queryObjectByMemberIdAndName(Integer memberId, String name) {
		return tagDao.queryObjectByMemberIdAndName(memberId, name);
	}

	@Override
	public List<TagEntity> queryListByClienteleId(Integer clienteleId) {
		return tagDao.queryListByClienteleId(clienteleId);
	}

	@Override
	@Transactional
	public TagEntity add(String name, Integer clienteleId, SessionMember sessionMember) {
		TagEntity tagEntity = tagDao.queryObjectByMemberIdAndName(sessionMember.getMemberId(), name.trim());
		if (tagEntity == null) {
			tagEntity = new TagEntity();
			tagEntity.setMemberId(sessionMember.getMemberId());
			tagEntity.setName(name.trim());
			tagEntity.setSuperiorId(sessionMember.getSuperiorId());
			tagDao.save(tagEntity);
		}

		if (clienteleId != null) {
			ClienteleEntity clienteleEntity = clienteleDao.queryObject(clienteleId);
			Assert.isNullApi(clienteleEntity, "该客户不存在");
			HashMap<String, Object> params = Maps.newHashMap();
			params.put("clienteleId", clienteleId);
			params.put("memberId", sessionMember.getMemberId());
			params.put("tagId", tagEntity.getId());
			int clienteleTagTotal = clienteleTagDao.queryTotal(params);
			if (clienteleTagTotal > 0) {
				throw new ApiException("该标签已存在");
			}
			ClienteleTagEntity clienteleTagEntity = new ClienteleTagEntity();
			clienteleTagEntity.setClienteleId(clienteleId);
			clienteleTagEntity.setMemberId(sessionMember.getMemberId());
			clienteleTagEntity.setTagId(tagEntity.getId());
			clienteleTagDao.save(clienteleTagEntity);
		}
		return tagEntity;
	}

	@Override
	public List<TagEntityVo> queryListVo(Map<String, Object> map) {
		return tagDao.queryListVo(map);
	}

	@Override
	public TagEntity queryObjectBySuperiorIdAndName(Integer superiorId, String name) {
		return tagDao.queryObjectBySuperiorIdAndName(superiorId, name);
	}

	@Override
	@Transactional
	public void deleteFull(Integer tagId) {
		tagDao.delete(tagId);
		clienteleTagDao.deleteByTagId(tagId);
	}

}
