package io.renren.cms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ApplyEntityVo;
import io.renren.api.vo.ApplyVo;
import io.renren.cms.dao.ApplyDao;
import io.renren.cms.dao.ApplyRecordDao;
import io.renren.cms.dao.CardDao;
import io.renren.cms.dao.TemplateApplyDao;
import io.renren.cms.dao.TemplateApplyUseRecordDao;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.TemplateApplyEntity;
import io.renren.cms.entity.TemplateApplyUseRecordEntity;
import io.renren.cms.service.ApplyService;
import io.renren.enums.MemberRoleEnum;
import io.renren.utils.validator.Assert;

/**
 * 报名服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("applyService")
public class ApplyServiceImpl implements ApplyService {

	@Autowired
	private ApplyDao applyDao;
	@Autowired
	private TemplateApplyDao templateApplyDao;
	@Autowired
	private ApplyRecordDao applyRecordDao;
	@Autowired
	private TemplateApplyUseRecordDao templateApplyUseRecordDao;
	@Autowired
	private CardDao cardDao;

	@Override
	public ApplyEntity queryObject(Integer id) {
		return applyDao.queryObject(id);
	}

	@Override
	public List<ApplyEntity> queryList(Map<String, Object> map) {
		return applyDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return applyDao.queryTotal(map);
	}

	@Override
	public void save(ApplyEntity apply) {
		applyDao.save(apply);
	}

	@Override
	public void update(ApplyEntity apply) {
		applyDao.update(apply);
	}

	@Override
	public void delete(Integer id) {
		applyDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		applyDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return applyDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return applyDao.logicDelBatch(ids);
	}

	@Override
	@Transactional
	public ApplyVo editApply(ApplyEntity applyEntity, SessionMember sessionMember) {
		TemplateApplyEntity templateApplyEntity = templateApplyDao.queryObject(applyEntity.getTemplateApplyId());
		Assert.isNullApi(templateApplyEntity, "该模板不存在");
		TemplateApplyEntity temp = new TemplateApplyEntity();
		temp.setId(templateApplyEntity.getId());
		temp.setUseCnt(templateApplyEntity.getUseCnt() + 1);
		templateApplyDao.update(temp);//更新使用量

		//使用记录
		TemplateApplyUseRecordEntity useRecordEntity = new TemplateApplyUseRecordEntity();
		useRecordEntity.setAvatarUrl(sessionMember.getPortrait());
		useRecordEntity.setMemberId(sessionMember.getMemberId());
		useRecordEntity.setOpenid(sessionMember.getOpenid());
		useRecordEntity.setTemplateApplyId(templateApplyEntity.getId());
		useRecordEntity.setTemplateImageExample(templateApplyEntity.getExamplePath());
		useRecordEntity.setUseTime(new Date());
		templateApplyUseRecordDao.save(useRecordEntity);

		if (MemberRoleEnum.STAFF.getCode().equals(sessionMember.getRole())) {
			//普通员工只能创建私有报名
			applyEntity.setPermission("private");
		}
		if (applyEntity.getId() == null) {
			applyEntity.setCtime(new Date());
			applyEntity.setUtime(new Date());
			applyEntity.setApplyCount(0);//报名人数默认0
			applyEntity.setIsDel(SystemConstant.F_STR);
			applyEntity.setMemberId(sessionMember.getMemberId());
			applyEntity.setOpenid(sessionMember.getOpenid());
			applyEntity.setSuperiorId(sessionMember.getSuperiorId());
			applyEntity.setViewCount(0);
			applyDao.save(applyEntity);
		} else {
			applyEntity.setUtime(new Date());
			applyDao.update(applyEntity);
		}

		ApplyVo applyVo = new ApplyVo();
		BeanUtil.copyProperties(applyEntity, applyVo);
		return applyVo;
	}

	@Override
	@Transactional
	public void joinApply(ApplyRecordEntity applyRecordEntity) {
		ApplyEntity applyEntity = applyDao.queryObject(applyRecordEntity.getApplyId());
		Assert.isNullApi(applyEntity, "该活动不存在");

		Date now = new Date();
		if (applyEntity.getStartTime().after(now)) {
			throw new ApiException("活动未开始");
		} else if (applyEntity.getStartTime().before(now) && applyEntity.getEndTime().after(now)) {
			//进行中
		} else {
			throw new ApiException("活动已结束");
		}

		ApplyRecordEntity applyRecordEntityDB = applyRecordDao.queryObjectByApplyIdAndJoinOpenid(applyEntity.getId(),
				applyRecordEntity.getJoinOpenid());
		if (applyRecordEntityDB != null) {
			throw new ApiException("您已参加，不能重复参加");
		}

		ApplyEntity temp = new ApplyEntity();
		temp.setId(applyEntity.getId());
		temp.setApplyCount(applyEntity.getApplyCount() + 1);
		applyDao.update(temp);//更新报名数量

		CardEntity cardEntity = cardDao.queryObjectByOpenid(applyRecordEntity.getJoinOpenid());
		if (cardEntity != null) {
			applyRecordEntity.setJoinPortrait(cardEntity.getPortrait());
			applyRecordEntity.setJoinNickName(cardEntity.getName());
		} else {
			applyRecordEntity.setJoinPortrait(
					StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
			applyRecordEntity.setJoinNickName(SystemConstant.DEFAULT_CARD_NAME);
		}
		applyRecordEntity.setCtime(new Date());
		applyRecordEntity.setIsDel(SystemConstant.F_STR);
		applyRecordEntity.setMemberId(applyEntity.getMemberId());
		applyRecordEntity.setSuperiorId(applyEntity.getSuperiorId());
		applyRecordEntity.setTemplateApplyId(applyEntity.getTemplateApplyId());
		applyRecordDao.save(applyRecordEntity);

	}

	@Override
	public List<ApplyEntity> queryListCreationRecord(Map<String, Object> map) {
		return applyDao.queryListCreationRecord(map);
	}

	@Override
	public List<ApplyEntityVo> queryListVo(Map<String, Object> map) {
		return applyDao.queryListVo(map);
	}

	@Override
	public int querySumViewCount(Map<String, Object> map) {
		return applyDao.querySumViewCount(map);
	}

	@Override
	public List<ApplyEntity> queryListShareRecord(Map<String, Object> map) {
		return applyDao.queryListShareRecord(map);
	}

	@Override
	@Transactional
	public void logicDelete(Integer applyId) {
		applyDao.logicDel(applyId);
		applyRecordDao.logicDelByApplyId(applyId);
	}
}
