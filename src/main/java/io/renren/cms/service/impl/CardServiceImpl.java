package io.renren.cms.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.CardEntityVo;
import io.renren.api.vo.CardVo;
import io.renren.cms.dao.CardAccessRecordDao;
import io.renren.cms.dao.CardDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.entity.CardAccessRecordEntity;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.CardService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.CertTypeEnum;
import io.renren.enums.MemberTypeEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.ProjectUtils;
import io.renren.utils.validator.Assert;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 名片服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Slf4j
@Service("cardService")
public class CardServiceImpl implements CardService {

	@Autowired
	private CardDao cardDao;
	@Autowired
	private CardAccessRecordDao cardAccessRecordDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private COSClient cosClient;

	@Override
	public CardEntity queryObject(Integer id) {
		return cardDao.queryObject(id);
	}

	@Override
	public List<CardEntity> queryList(Map<String, Object> map) {
		return cardDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return cardDao.queryTotal(map);
	}

	@Override
	public void save(CardEntity card) {
		cardDao.save(card);
	}

	@Override
	public void update(CardEntity card) {
		cardDao.update(card);
	}

	@Override
	public void delete(Integer id) {
		cardDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		cardDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return cardDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return cardDao.logicDelBatch(ids);
	}

	@Override
	public CardEntity queryObjectByOpenid(String openid) {
		return cardDao.queryObjectByOpenid(openid);
	}

	@Override
	@Transactional
	public CardVo view(String openid, Integer cardId) {
		CardEntity cardEntity = cardDao.queryObject(cardId);
		Assert.isNullApi(cardEntity, "名片不存在");
		CardVo cardVo = new CardVo();
		BeanUtil.copyProperties(cardEntity, cardVo);

		//访问记录
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("cardId", cardId);
		params.put("sidx", "access_time");
		params.put("order", "desc");
		params.put("offset", 0);
		params.put("limit", 5);
		List<CardAccessRecordEntity> recordList = cardAccessRecordDao.queryList(params);
		int accessCount = cardAccessRecordDao.queryTotal(params);
		cardVo.setAccessPortraits(
				recordList.stream().map(CardAccessRecordEntity::getPortrait).collect(Collectors.toList()));
		cardVo.setAccessCount(accessCount);

		if(!StringUtils.equals(openid, cardEntity.getOpenid())) {
			//添加访问记录
			CardAccessRecordEntity cardAccessRecordEntity = cardAccessRecordDao.queryObjectByOpenidAndCardId(openid,
					cardId);
			Date now = new Date();
			CardEntity viewCard = cardDao.queryObjectByOpenid(openid);
			if (cardAccessRecordEntity == null) {
				cardAccessRecordEntity = new CardAccessRecordEntity();
				cardAccessRecordEntity.setCtime(now);
				cardAccessRecordEntity.setAccessTime(now);
				cardAccessRecordEntity.setCardId(cardId);
				cardAccessRecordEntity.setOpenid(openid);//访问人微信用户ID
				cardAccessRecordEntity.setCardOpenid(cardEntity.getOpenid());//名片归属人微信用户ID

				if (viewCard != null) {
					//查看人已经有自己名片
					cardAccessRecordEntity.setCompany(viewCard.getCompany());
					cardAccessRecordEntity.setPortrait(viewCard.getPortrait());
					cardAccessRecordEntity.setPosition(viewCard.getPosition());
					cardAccessRecordEntity.setName(viewCard.getName());
				} else {
					cardAccessRecordEntity.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
					cardAccessRecordEntity.setPosition(SystemConstant.DEFAULT_CARD_POSITION);
					cardAccessRecordEntity.setCompany(SystemConstant.DEFAULT_CARD_COMPANY);
					cardAccessRecordEntity.setName(SystemConstant.DEFAULT_CARD_NAME);
				}
				cardAccessRecordDao.save(cardAccessRecordEntity);
			} else {
				if (viewCard != null) {
					//查看人已经有自己名片
					cardAccessRecordEntity.setCompany(viewCard.getCompany());
					cardAccessRecordEntity.setPortrait(viewCard.getPortrait());
					cardAccessRecordEntity.setPosition(viewCard.getPosition());
					cardAccessRecordEntity.setName(viewCard.getName());
				} 
				cardAccessRecordEntity.setAccessTime(now);
				cardAccessRecordDao.update(cardAccessRecordEntity);
			}
		}

		MemberEntity cardMember = memberDao.queryObjectByOpenid(cardEntity.getOpenid());
		if(cardMember != null) {
			cardVo.setCertType(cardMember.getCertType());
			cardVo.setType(cardMember.getType());
		}
		return cardVo;
	}

	@Override
	@Transactional
	public void modify(CardEntity cardEntity) {
		CardEntity cardEntityOld = cardDao.queryObjectByOpenid(cardEntity.getOpenid());
		if (cardEntityOld != null) {
			//更新名片
			BeanUtil.copyProperties(cardEntity, cardEntityOld, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			cardDao.update(cardEntityOld);
		} else {
			//创建名片
			Date now = new Date();
			cardEntity.setCtime(now);
			cardEntity.setUtime(now);
			cardEntity.setIsDel(SystemConstant.F_STR);
			cardDao.save(cardEntity);
		}
		MemberEntity memberEntity = memberDao.queryObjectByOpenid(cardEntity.getOpenid());
		if(memberEntity != null) {
			//同步更新会员相关属性
			MemberEntity temp = new MemberEntity();
			temp.setId(memberEntity.getId());
			temp.setPortrait(cardEntityOld.getPortrait());
			temp.setNickname(cardEntityOld.getName());
			temp.setRealName(cardEntityOld.getName());
			temp.setCompany(cardEntityOld.getCompany());
			memberDao.update(temp);
		}
	}

	@Override
	@Transactional
	public CardVo info(String openid) {
		CardEntity cardEntity = cardDao.queryObjectByOpenid(openid);
		CardVo cardVo = new CardVo();
		if (cardEntity == null) {
			Date now = new Date();
			cardEntity = new CardEntity();
			cardEntity.setCtime(now);
			cardEntity.setIsDel(SystemConstant.F_STR);
			cardEntity.setOpenid(openid);
			cardEntity.setQrcode(null);
			cardEntity.setUtime(now);
			cardEntity.setName(SystemConstant.DEFAULT_CARD_NAME);
			cardEntity.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
			cardEntity.setCompany(SystemConstant.DEFAULT_CARD_COMPANY);
			cardEntity.setPosition(SystemConstant.DEFAULT_CARD_POSITION);
			cardDao.save(cardEntity);
			
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			try {
				File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
						StrUtil.format(SystemConstant.APP_PAGE_PATH_CARD_DETAIL, cardEntity.getId()), 280, false, null, false);
				String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
				CardEntity cardTemp = new CardEntity();
				cardTemp.setId(cardEntity.getId());
				cardTemp.setQrcode(yykjProperties.getImagePrefixUrl().concat(key));
				cardDao.update(cardTemp);
			} catch (WxErrorException e) {
				log.error("===生成名片小程序码异常：{}", e.getMessage());
			}

			cardVo.setId(cardEntity.getId());
			cardVo.setName(cardEntity.getName());
			cardVo.setPortrait(cardEntity.getPortrait());
			cardVo.setCompany(cardEntity.getCompany());
			cardVo.setPosition(cardEntity.getPosition());
			cardVo.setType(MemberTypeEnum.COMMON.getCode());
			cardVo.setCertType(CertTypeEnum.UNKNOWN.getCode());
			return cardVo;
		}

		//已有名片
		BeanUtil.copyProperties(cardEntity, cardVo);
		MemberEntity memberEntity = memberDao.queryObjectByOpenid(openid);
		if (memberEntity != null) {
			//已经是付费会员
			cardVo.setType(memberEntity.getType());
			cardVo.setCertType(memberEntity.getCertType());
		}
		return cardVo;
	}

    @Override
    public List<CardEntityVo> queryListVo(Map<String, Object> map) {
        return cardDao.queryListVo(map);
    }

}
