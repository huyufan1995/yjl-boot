package io.renren.cms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import io.renren.api.vo.CaseEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.CaseVo;
import io.renren.cms.dao.CardDao;
import io.renren.cms.dao.CaseBrowseDao;
import io.renren.cms.dao.CaseDao;
import io.renren.cms.dao.CasePraiseDao;
import io.renren.cms.dao.CaseShareDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.CaseBrowseEntity;
import io.renren.cms.entity.CaseEntity;
import io.renren.cms.entity.CaseShareEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.CaseService;
import io.renren.enums.MemberRoleEnum;
import io.renren.utils.Query;
import io.renren.utils.validator.Assert;

/**
 * 案例服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Service("caseService")
public class CaseServiceImpl implements CaseService {

	@Autowired
	private CaseDao caseDao;
	@Autowired
	private CaseBrowseDao caseBrowseDao;
	@Autowired
	private CasePraiseDao casePraiseDao;
	@Autowired
	private CardDao cardDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private CaseShareDao caseShareDao;

	@Override
	public CaseEntity queryObject(Integer id) {
		return caseDao.queryObject(id);
	}

	@Override
	public List<CaseEntity> queryList(Map<String, Object> map) {
		return caseDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return caseDao.queryTotal(map);
	}

	@Override
	public void save(CaseEntity caseEntity) {
		caseDao.save(caseEntity);
	}

	@Override
	public void update(CaseEntity caseEntity) {
		caseDao.update(caseEntity);
	}

	@Override
	public void delete(Integer id) {
		caseDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		caseDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return caseDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return caseDao.logicDelBatch(ids);
	}

	@Override
	@Transactional
	public CaseVo caseInfo(Integer caseId, String openid, Integer shareMemberId) {
		CaseEntity caseEntity = caseDao.queryObject(caseId);
		Assert.isNullApi(caseEntity, "该案例不存在");
		if(shareMemberId == null) {
			//没有分享人按创建人为准
			shareMemberId = caseEntity.getMemberId();
		}
		CaseShareEntity caseShareEntity = caseShareDao.queryObjectByCaseIdAndShareMemberId(caseId, shareMemberId);
		if(caseShareEntity == null) {
			caseShareEntity = new CaseShareEntity();
			caseShareEntity.setCaseId(caseId);
			caseShareEntity.setShareMemberId(shareMemberId);
			caseShareEntity.setShareTime(new Date());
			MemberEntity shareMember = memberDao.queryObject(shareMemberId);
			if(shareMember.getRole().equals(MemberRoleEnum.BOSS.getCode())) {
				shareMember.setSuperiorId(shareMember.getId());
			}
			caseShareEntity.setShareSuperiorId(shareMember.getSuperiorId());
			caseShareDao.save(caseShareEntity);
		}
		CaseVo caseVo = new CaseVo();
		BeanUtil.copyProperties(caseEntity, caseVo);
		HashMap<String, Object> browseParam = Maps.newHashMap();
		browseParam.put("caseId", caseId);
		browseParam.put("page", 1);
		browseParam.put("limit", 5);
		Query browseQuery = new Query(browseParam);
		List<CaseBrowseEntity> caseBrowseList = caseBrowseDao.queryList(browseQuery);
		caseVo.setBrowsePortraits(
				caseBrowseList.stream().map(CaseBrowseEntity::getBrowsePortrait).collect(Collectors.toList()));

		HashMap<String, Object> praiseParam = Maps.newHashMap();
		praiseParam.put("caseId", caseId);
		praiseParam.put("openid", openid);
		int praiseTotal = casePraiseDao.queryTotal(praiseParam);
		caseVo.setPraise(praiseTotal > 0);

		CaseBrowseEntity caseBrowseEntity = caseBrowseDao.queryObjectByCaseIdAndBrowseOpenidAndShareMemberId(caseId,
				openid, shareMemberId);
		CardEntity cardEntity = cardDao.queryObjectByOpenid(openid);
		if (caseBrowseEntity == null) {
			//添加浏览记录
			caseBrowseEntity = new CaseBrowseEntity();
			caseBrowseEntity.setCaseId(caseId);
			caseBrowseEntity.setBrowseOpenid(openid);
			caseBrowseEntity.setShareMemberId(shareMemberId);
			caseBrowseEntity.setBrowseTime(new Date());
			if (cardEntity != null) {
				caseBrowseEntity.setBrowseCompany(cardEntity.getCompany());
				caseBrowseEntity.setBrowseName(cardEntity.getName());
				caseBrowseEntity.setBrowsePortrait(cardEntity.getPortrait());

			} else {
				caseBrowseEntity.setBrowseCompany(SystemConstant.DEFAULT_CARD_COMPANY);
				caseBrowseEntity.setBrowseName(SystemConstant.DEFAULT_CARD_NAME);
				caseBrowseEntity.setBrowsePortrait(
						StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
			}
			caseBrowseDao.save(caseBrowseEntity);
		} else {
			if (cardEntity != null) {
				caseBrowseEntity.setBrowseCompany(cardEntity.getCompany());
				caseBrowseEntity.setBrowseName(cardEntity.getName());
				caseBrowseEntity.setBrowsePortrait(cardEntity.getPortrait());
			}
			caseBrowseEntity.setBrowseTime(new Date());
			caseBrowseDao.update(caseBrowseEntity);
		}

		CaseEntity tempCase = new CaseEntity();
		tempCase.setId(caseEntity.getId());
		tempCase.setViewCnt(caseEntity.getViewCnt() + 1);
		caseDao.update(tempCase);

		//分享人电话
		if (shareMemberId == null) {
			//自己查看详情
			CardEntity shareCard = cardDao.queryObjectByOpenid(openid);
			if (shareCard != null) {
				caseVo.setMobile(shareCard.getMobile());
			}
		} else {
			MemberEntity shareMember = memberDao.queryObject(shareMemberId);
			if (shareMember != null) {
				if (StringUtils.isNotBlank(shareMember.getMobile())) {
					caseVo.setMobile(shareMember.getMobile());
				} else {
					CardEntity shareCard = cardDao.queryObjectByOpenid(shareMember.getOpenid());
					if (shareCard != null) {
						caseVo.setMobile(shareCard.getMobile());
					}
				}
			}
		}

		//创建人姓名
		MemberEntity memberEntity = memberDao.queryObject(caseVo.getMemberId());
		if (memberEntity != null) {
			caseVo.setRealName(memberEntity.getRealName());
		}

		return caseVo;
	}

	@Override
	public List<CaseVo> queryListByMemberIdOrSuperiorId(Map<String, Object> map) {
		return caseDao.queryListByMemberIdOrSuperiorId(map);
	}

	@Override
	public List<CaseVo> queryListMyShare(Map<String, Object> map) {
		return caseDao.queryListMyShare(map);
	}

	@Override
	public List<CaseEntityVo> queryListVo(Map<String, Object> map) {
		return caseDao.queryListVo(map);
	}

}
