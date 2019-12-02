package io.renren.cms.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import io.renren.api.constant.SystemConstant;
import io.renren.api.exception.ApiException;
import io.renren.cms.dao.ForbiddenDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.entity.ForbiddenMemberEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.MemberService;
import io.renren.cms.service.WxUserService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.renren.service.SysConfigService;
import io.renren.utils.ProjectUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.renren.api.dto.SessionMember;
import io.renren.enums.MemberTypeEnum;
import io.renren.service.TokenService;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员服务实现
 * @author yujia
 * @email yujiain2008@163.com
 *
 */
@Slf4j
@Service("memberService")
public class MemberServiceImpl implements MemberService {

	@Value("${tencent.cos.imagePrefixUrl}")
	private String imagePrefixUrl;


	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ForbiddenDao forbiddenDao;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private SysConfigService sysConfigService;

	
	@Override
	public MemberEntity queryObject(Integer id) {
		return memberDao.queryObject(id);
	}

	@Override
	public List<MemberEntity> queryList(Map<String, Object> map) {
		return memberDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return memberDao.queryTotal(map);
	}

	@Override
	public void save(MemberEntity member) {
		memberDao.save(member);
	}

	@Override
	public void update(MemberEntity member) {
		memberDao.update(member);
	}

	@Override
	public void delete(Integer id) {
		memberDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		memberDao.deleteBatch(ids);
	}

	@Override
	public int logicDel(Integer id) {
		return memberDao.logicDel(id);
	}

	@Override
	public int logicDelBatch(List<Integer> ids) {
		return memberDao.logicDelBatch(ids);
	}

	@Override
	public MemberEntity queryObjectByOpenid(String openid) {
		return memberDao.queryObjectByOpenid(openid);
	}

	@Override
	public SessionMember login(String openid) {
		MemberEntity memberEntity = memberDao.queryObjectByOpenid(openid);
		SessionMember sessionMember = new SessionMember();
		if(memberEntity ==null ){
			MemberEntity memberEntity1 = new MemberEntity();
			memberEntity1.setOpenid(openid);
			memberEntity1.setCtime(new Date());
			memberEntity1.setIsDel("f");
			memberEntity1.setStatus("normal");
			memberEntity1.setType(MemberTypeEnum.COMMON.getCode());
			memberEntity1.setShowVip("f");
			memberDao.save(memberEntity1);
			return getSessionMember(memberEntity1, sessionMember);
		}
		if (StringUtils.equals("freeze", memberEntity.getStatus())) {
			String forbiddenMsg = "该账号已被封禁";
			ForbiddenMemberEntity forbiddenMemberEntity = forbiddenDao.queryObjectByOpenid(openid);
			if (forbiddenMemberEntity != null) {
				forbiddenMsg = forbiddenMemberEntity.getForbiddenMsg();
			}
			throw new ApiException(forbiddenMsg, 10001, forbiddenMemberEntity);
		}
		return getSessionMember(memberEntity, sessionMember);
	}

	private SessionMember getSessionMember(MemberEntity memberEntity, SessionMember sessionMember) {
		Map<String, Object> map = tokenService.createToken(memberEntity.getId());
		sessionMember.setOpenid(memberEntity.getOpenid());
		sessionMember.setToken(map.get("token").toString());
		sessionMember.setMemberId(memberEntity.getId());
		sessionMember.setShowVip(memberEntity.getShowVip());
		sessionMember.setVerify(memberEntity.getVerify());
		String mobile = sysConfigService.getValue("DEFAULT_MOBILE", "");
		//联系我们 默认拨打电话
		sessionMember.setDialPhone(mobile);
		if (StringUtils.isNotBlank(memberEntity.getMobile())) {
			sessionMember.setMobile(memberEntity.getMobile());
/*
			sessionMember.setMobileCipher(memberEntity.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
*/
		}
		sessionMember.setNickname(memberEntity.getNickname());
		sessionMember.setPortrait(memberEntity.getPortrait());
		sessionMember.setType(memberEntity.getType());//会员类型
		sessionMember.setQrCode(memberEntity.getQrCode());
		sessionMember.setCode(memberEntity.getCode());
		return sessionMember;
	}

	@Override
	public MemberEntity queryObjectByCode(String code) {
		return null;
	}

	@Override
	public List<MemberEntity> queryListByMemberBanner(Map<String, Object> map) {
		return memberDao.queryListByMemberBanner(map);
	}

	@Override
	public List<MemberEntity> queryListByIsCollect(Map<String, Object> map) {
		return memberDao.queryListByIsCollect(map);
	}

	@Override
	public void updateVerify(String verify, String openid) {
		memberDao.updateVerify(verify,openid);
	}

	@Override
	public List<MemberEntity> queryAddressAndNationalityInfo() {
		return memberDao.queryAddressAndNationalityInfo();
	}

	@Override
	public List<MemberEntity> queryListLikeAll(Map<String, Object> params) {
		return memberDao.queryListLikeAll(params);
	}


}
