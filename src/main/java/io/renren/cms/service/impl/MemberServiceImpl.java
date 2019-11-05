package io.renren.cms.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.cms.dao.MemberDao;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import org.apache.commons.lang3.StringUtils;
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
	private TokenService tokenService;

	
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
/*		if (memberEntity == null || BindStatusEnum.UNBIND.getCode().equals(memberEntity.getBindStatus())
				|| BindStatusEnum.NOT.getCode().equals(memberEntity.getBindStatus())) {
			//不是付费会员或者已被解绑
			CardEntity cardEntity = cardDao.queryObjectByOpenid(openid);
			if (cardEntity != null) {
				sessionMember.setNickname(cardEntity.getName());
				sessionMember.setPortrait(cardEntity.getPortrait());
			}
			sessionMember.setOpenid(openid);
			return sessionMember;
		}*/

/*		if (StringUtils.equals("freeze", memberEntity.getStatus())) {
			String forbiddenMsg = "该账号已被封禁";
			ForbiddenMemberEntity forbiddenMemberEntity = null;
			if (StringUtils.equals(MemberRoleEnum.BOSS.getCode(), memberEntity.getRole())) {
				forbiddenMemberEntity = forbiddenDao.queryObjectBySuperiorId(memberEntity.getId());
			} else {
				forbiddenMemberEntity = forbiddenDao.queryObjectByOpenid(openid);
			}
			if (forbiddenMemberEntity != null) {
				forbiddenMsg = forbiddenMemberEntity.getForbiddenMsg();
			}
			throw new ApiException(forbiddenMsg, 10001, forbiddenMemberEntity);
		}*/

		Map<String, Object> map = tokenService.createToken(memberEntity.getId());
		sessionMember.setOpenid(memberEntity.getOpenid());
		sessionMember.setToken(map.get("token").toString());
		sessionMember.setMemberId(memberEntity.getId());
		if (StringUtils.isNotBlank(memberEntity.getMobile())) {
			sessionMember.setMobile(memberEntity.getMobile());
			sessionMember.setMobileCipher(memberEntity.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
		}
		sessionMember.setNickname(memberEntity.getNickname());
		sessionMember.setType(MemberTypeEnum.VIP.getCode());//vip付费会员
		return sessionMember;
	}

	@Override
	public MemberEntity queryObjectByCode(String code) {
		return null;
	}


}
