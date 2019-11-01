package io.renren.cms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.renren.api.dto.SessionMember;
import io.renren.api.vo.TemplateWebsiteLayoutEntityVo;
import io.renren.cms.dao.TemplateWebsiteDao;
import io.renren.cms.dao.TemplateWebsiteLayoutDao;
import io.renren.cms.dao.TemplateWebsiteMetadataDao;
import io.renren.cms.dao.TemplateWebsiteUseRecordDao;
import io.renren.cms.entity.TemplateWebsiteEntity;
import io.renren.cms.entity.TemplateWebsiteLayoutEntity;
import io.renren.cms.entity.TemplateWebsiteMetadataEntity;
import io.renren.cms.entity.TemplateWebsiteUseRecordEntity;
import io.renren.cms.service.TemplateWebsiteLayoutService;
import io.renren.enums.MemberRoleEnum;
import io.renren.utils.validator.Assert;

/**
 * 官网模板布局服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("templateWebsiteLayoutService")
public class TemplateWebsiteLayoutServiceImpl implements TemplateWebsiteLayoutService {

	@Autowired
	private TemplateWebsiteLayoutDao templateWebsiteLayoutDao;
	@Autowired
	private TemplateWebsiteMetadataDao templateWebsiteMetadataDao;
	@Autowired
	private TemplateWebsiteUseRecordDao templateWebsiteUseRecordDao;
	@Autowired
	private TemplateWebsiteDao templateWebsiteDao;

	@Override
	public TemplateWebsiteLayoutEntity queryObject(Integer id) {
		return templateWebsiteLayoutDao.queryObject(id);
	}

	@Override
	public List<TemplateWebsiteLayoutEntity> queryList(Map<String, Object> map) {
		return templateWebsiteLayoutDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateWebsiteLayoutDao.queryTotal(map);
	}

	@Override
	public void save(TemplateWebsiteLayoutEntity templateWebsiteLayout) {
		templateWebsiteLayoutDao.save(templateWebsiteLayout);
	}

	@Override
	public void update(TemplateWebsiteLayoutEntity templateWebsiteLayout) {
		templateWebsiteLayoutDao.update(templateWebsiteLayout);
	}

	@Override
	public void delete(Integer id) {
		templateWebsiteLayoutDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateWebsiteLayoutDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return templateWebsiteLayoutDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return templateWebsiteLayoutDao.logicDelBatch(ids);
	}

	@Override
	public TemplateWebsiteLayoutEntity queryObjectByMemberId(Integer memberId) {
		return templateWebsiteLayoutDao.queryObjectByMemberId(memberId);
	}

	@Override
	@Transactional
	public void saveConf(Integer templateId, String layout, String metadata, SessionMember sessionMember) {
		TemplateWebsiteEntity templateWebsiteEntity = templateWebsiteDao.queryObject(templateId);
		Assert.isNullApi(templateWebsiteEntity, "该模板不存在");
		templateWebsiteEntity.setUseCnt(templateWebsiteEntity.getUseCnt() + 1);
		templateWebsiteDao.update(templateWebsiteEntity);//更新使用量
		//查询上级元数据
		TemplateWebsiteMetadataEntity metadataEntity = templateWebsiteMetadataDao.queryObjectByMemberId(sessionMember.getSuperiorId());
		if(metadataEntity == null) {
			//没有元数据
			metadataEntity = new TemplateWebsiteMetadataEntity();
			metadataEntity.setCtime(new Date());
			//上级ID
			metadataEntity.setMemberId(sessionMember.getSuperiorId());
			metadataEntity.setOperator(sessionMember.getMemberId());
			metadataEntity.setUtime(new Date());
			if(MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())) {
				//业务员使用默认
				metadata = templateWebsiteEntity.getMetadata();
			}
			metadataEntity.setMetadata(metadata);
			templateWebsiteMetadataDao.save(metadataEntity);
		} else {
			//管理员提交了元数据
			if (MemberRoleEnum.ADMIN.getCode().equals(sessionMember.getRole())
					|| MemberRoleEnum.BOSS.getCode().equals(sessionMember.getRole())) {
				if (StringUtils.isNotBlank(metadata)) {
					metadataEntity.setUtime(new Date());
					metadataEntity.setOperator(sessionMember.getMemberId());
					metadataEntity.setMetadata(metadata);
					templateWebsiteMetadataDao.update(metadataEntity);
				}
			}
		}
		
		//查询自己的布局
		TemplateWebsiteLayoutEntity layoutEntity = templateWebsiteLayoutDao.queryObjectByMemberId(sessionMember.getMemberId());
		if (layoutEntity != null) {
			layoutEntity.setLayout(layout);
			layoutEntity.setTemplateId(templateId);
			layoutEntity.setUtime(new Date());
			layoutEntity.setMetadataId(metadataEntity.getId());//元数据
			templateWebsiteLayoutDao.update(layoutEntity);
		} else {
			layoutEntity = new TemplateWebsiteLayoutEntity();
			layoutEntity.setCtime(new Date());
			layoutEntity.setLayout(layout);
			layoutEntity.setMemberId(sessionMember.getMemberId());
			layoutEntity.setOpenid(sessionMember.getOpenid());
			layoutEntity.setTemplateId(templateId);
			layoutEntity.setUtime(new Date());
			layoutEntity.setMetadataId(metadataEntity.getId());//元数据
			templateWebsiteLayoutDao.save(layoutEntity);
		}
		
		//添加使用记录
		TemplateWebsiteUseRecordEntity recordEntity = templateWebsiteUseRecordDao.queryObjectByTemplateWebsiteIdAndMemberId(templateId, sessionMember.getMemberId());
		if(recordEntity == null) {
			recordEntity = new TemplateWebsiteUseRecordEntity();
			recordEntity.setAvatarUrl(sessionMember.getPortrait());
			recordEntity.setMemberId(sessionMember.getMemberId());
			recordEntity.setOpenid(sessionMember.getOpenid());
			recordEntity.setTemplateImageExample(templateWebsiteEntity.getExamplePath());
			recordEntity.setTemplateWebsiteId(templateWebsiteEntity.getId());
			recordEntity.setUseTime(new Date());
			templateWebsiteUseRecordDao.save(recordEntity);
		} else {
			recordEntity.setUseTime(new Date());
			templateWebsiteUseRecordDao.update(recordEntity);
		}
		
	}

	@Override
	public TemplateWebsiteLayoutEntity queryObjectByOpenid(String openid) {
		return templateWebsiteLayoutDao.queryObjectByOpenid(openid);
	}

	@Override
	public List<TemplateWebsiteLayoutEntityVo> queryListVo(Map<String, Object> map) {
		return templateWebsiteLayoutDao.queryListVo(map);
	}

	@Override
	public int deleteByMemberId(Integer memberId) {
		return templateWebsiteLayoutDao.deleteByMemberId(memberId);
	}

}
