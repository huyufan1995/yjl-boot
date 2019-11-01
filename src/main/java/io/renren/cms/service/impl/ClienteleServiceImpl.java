package io.renren.cms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ClienteleEntityVo;
import io.renren.api.vo.ClienteleVo;
import io.renren.cms.dao.ClienteleAllotRecordDao;
import io.renren.cms.dao.ClienteleDao;
import io.renren.cms.dao.ClienteleRemarkDao;
import io.renren.cms.dao.ClienteleTagDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.dao.TagDao;
import io.renren.cms.entity.ClienteleAllotRecordEntity;
import io.renren.cms.entity.ClienteleEntity;
import io.renren.cms.entity.ClienteleRemarkEntity;
import io.renren.cms.entity.ClienteleTagEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.ClienteleService;
import io.renren.utils.validator.Assert;

/**
 * 客户服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("clienteleService")
public class ClienteleServiceImpl implements ClienteleService {

	@Autowired
	private ClienteleDao clienteleDao;
	@Autowired
	private ClienteleTagDao clienteleTagDao;
	@Autowired
	private TagDao tagDao;
	@Autowired
	private ClienteleRemarkDao clienteleRemarkDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ClienteleAllotRecordDao clienteleAllotRecordDao;

	@Override
	public ClienteleEntity queryObject(Integer id) {
		return clienteleDao.queryObject(id);
	}

	@Override
	public List<ClienteleEntity> queryList(Map<String, Object> map) {
		return clienteleDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return clienteleDao.queryTotal(map);
	}

	@Override
	public void save(ClienteleEntity clientele) {
		clienteleDao.save(clientele);
	}

	@Override
	public void update(ClienteleEntity clientele) {
		clienteleDao.update(clientele);
	}

	@Override
	public void delete(Integer id) {
		clienteleDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		clienteleDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return clienteleDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return clienteleDao.logicDelBatch(ids);
	}

	@Override
	@Transactional
	public void add(ClienteleEntity clienteleEntity, String tags, String remark) {
		
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("superiorId", clienteleEntity.getSuperiorId());
		params.put("mobile", clienteleEntity.getMobile());
		int queryTotal = clienteleDao.queryTotal(params);
		
		if(queryTotal > 0) {
			throw new ApiException(clienteleEntity.getMobile() + "，该客户数据系统已存在");
		}
		
		Date now = new Date();
		if(StringUtils.isBlank(clienteleEntity.getPortrait())) {
			clienteleEntity.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
		}
		clienteleEntity.setCtime(now);
		clienteleEntity.setIsDel(SystemConstant.F_STR);
		clienteleDao.save(clienteleEntity);

		//保存标签
		if (StringUtils.isNotBlank(tags)) {
			String[] tagsArray = tags.split(",");
			for (String tagId : tagsArray) {
				ClienteleTagEntity clienteleTagEntity = new ClienteleTagEntity();
				clienteleTagEntity.setClienteleId(clienteleEntity.getId());
				clienteleTagEntity.setMemberId(clienteleEntity.getMemberId());
				clienteleTagEntity.setTagId(Integer.valueOf(tagId));
				clienteleTagDao.save(clienteleTagEntity);
			}
		}

		//保存备注
		if (StringUtils.isNotBlank(remark)) {
			ClienteleRemarkEntity remarkEntity = new ClienteleRemarkEntity();
			remarkEntity.setClienteleId(clienteleEntity.getId());
			remarkEntity.setContent(remark);
			remarkEntity.setCtime(now);
			remarkEntity.setIsDel(SystemConstant.F_STR);
			remarkEntity.setMemberId(clienteleEntity.getMemberId());
			clienteleRemarkDao.save(remarkEntity);
		}
	}

	@Override
	@Transactional
	public void modify(ClienteleEntity clienteleEntity, String tags) {
		ClienteleEntity clienteleDB = clienteleDao.queryObject(clienteleEntity.getId());
		Assert.isNullApi(clienteleDB, "修改的客户不存在");
		clienteleDao.update(clienteleEntity);
		//保存标签
		if (StringUtils.isNotBlank(tags)) {
			//删除已有全部标签
			clienteleTagDao.deleteByClienteleIdAndMemberId(clienteleDB.getId(), clienteleDB.getMemberId());
			String[] tagsArray = tags.split(",");
			for (String tagId : tagsArray) {
				ClienteleTagEntity clienteleTagEntity = new ClienteleTagEntity();
				clienteleTagEntity.setClienteleId(clienteleEntity.getId());
				clienteleTagEntity.setMemberId(clienteleDB.getMemberId());
				clienteleTagEntity.setTagId(Integer.valueOf(tagId));
				clienteleTagDao.save(clienteleTagEntity);
			}
		}
	}

	@Override
	public ClienteleEntity queryObjectByMobile(String mobile) {
		return clienteleDao.queryObjectByMobile(mobile);
	}

	@Override
	public List<ClienteleVo> querySearchList(Map<String, Object> map) {
		List<ClienteleVo> clienteleVoList = clienteleDao.querySearchList(map);
		for (ClienteleVo clienteleVo : clienteleVoList) {
			clienteleVo.setTags(tagDao.queryListByClienteleId(clienteleVo.getId()));
			clienteleVo.setRemarks(clienteleRemarkDao.queryListByClienteleId(clienteleVo.getId()));
		}
		return clienteleVoList;
	}

	@Override
	public int updateClearMemberId(Integer memberId) {
		return clienteleDao.updateClearMemberId(memberId);
	}

	@Override
	@Transactional
	public void changeMember(Integer clienteleId, Integer targetMemberId, Integer operationMemberId) {
		ClienteleEntity clienteleDB = clienteleDao.queryObject(clienteleId);
		Assert.isNullApi(clienteleDB, "该客户不存在");
		if (clienteleDB.getMemberId().equals(targetMemberId)) {
			throw new ApiException("您已经是该客户的负责人");
		}
		MemberEntity targetMember = memberDao.queryObject(targetMemberId);
		Assert.isNullApi(targetMember, "迁移用户不存在");
		ClienteleEntity clienteleEntity = new ClienteleEntity();
		clienteleEntity.setId(clienteleId);
		clienteleEntity.setMemberId(targetMemberId);//更新当前负责人
		clienteleDao.update(clienteleEntity);
		
		//处理标签
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("clienteleId", clienteleDB.getId());
//		params.put("memberId", clienteleDB.getMemberId());
		List<ClienteleTagEntity> clienteleTagList = clienteleTagDao.queryList(params);
		for (ClienteleTagEntity clienteleTagEntity : clienteleTagList) {
//			TagEntity tagEntity = tagDao.queryObject(clienteleTagEntity.getTagId());
//			TagEntity targetTagEntity = tagDao.queryObjectByMemberIdAndName(targetMemberId, tagEntity.getName());
//			if(targetTagEntity == null) {
//				targetTagEntity = new TagEntity();
//				targetTagEntity.setMemberId(targetMemberId);
//				targetTagEntity.setName(tagEntity.getName());
//				targetTagEntity.setSuperiorId(tagEntity.getSuperiorId());
//				tagDao.save(targetTagEntity);
//			} else {
				clienteleTagEntity.setMemberId(targetMemberId);
//				clienteleTagEntity.setTagId(targetTagEntity.getId());
				clienteleTagDao.update(clienteleTagEntity);
//			}
		}
		
		//添加更新记录
		ClienteleAllotRecordEntity allotRecord = new ClienteleAllotRecordEntity();
		allotRecord.setClienteleId(clienteleId);
		allotRecord.setCtime(new Date());
		allotRecord.setOperationMember(operationMemberId);
		allotRecord.setSourceMember(clienteleDB.getMemberId());
		allotRecord.setTargetMember(targetMemberId);
		clienteleAllotRecordDao.save(allotRecord);
	}

	@Override
	public ClienteleEntity queryObjectByMobileAndMemberId(String mobile, Integer memberId) {
		return clienteleDao.queryObjectByMobileAndMemberId(mobile, memberId);
	}

	@Override
	public List<ClienteleEntityVo> queryListVo(Map<String, Object> map) {
		return clienteleDao.queryListVo(map);
	}

	@Override
	public ClienteleEntityVo queryObjectVo(Integer id) {
		return clienteleDao.queryObjectVo(id);
	}

}
