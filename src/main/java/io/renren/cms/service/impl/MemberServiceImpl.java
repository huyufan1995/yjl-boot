package io.renren.cms.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qcloud.cos.COSClient;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.MemberEntityVo;
import io.renren.api.vo.StaffVo;
import io.renren.cms.dao.CardDao;
import io.renren.cms.dao.ClienteleAllotRecordDao;
import io.renren.cms.dao.ClienteleDao;
import io.renren.cms.dao.DeptDao;
import io.renren.cms.dao.DeptMemberDao;
import io.renren.cms.dao.ForbiddenDao;
import io.renren.cms.dao.MemberDao;
import io.renren.cms.dao.MemberRemovalRecordDao;
import io.renren.cms.dao.TemplateWebsiteLayoutDao;
import io.renren.cms.dao.WxUserDao;
import io.renren.cms.entity.CardEntity;
import io.renren.cms.entity.ClienteleAllotRecordEntity;
import io.renren.cms.entity.ClienteleEntity;
import io.renren.cms.entity.DeptEntity;
import io.renren.cms.entity.DeptMemberEntity;
import io.renren.cms.entity.ForbiddenMemberEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.entity.MemberRemovalRecordEntity;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.MemberService;
import io.renren.enums.BindStatusEnum;
import io.renren.enums.CertTypeEnum;
import io.renren.enums.MemberRoleEnum;
import io.renren.enums.MemberTypeEnum;
import io.renren.service.TokenService;
import io.renren.utils.ProjectUtils;
import io.renren.utils.validator.Assert;
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
	private COSClient cosClient;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private WxUserDao wxUserDao;
	@Autowired
	private ClienteleDao clienteleDao;
	@Autowired
	private CardDao cardDao;
	@Autowired
	private ClienteleAllotRecordDao clienteleAllotRecordDao;
	@Autowired
	private MemberRemovalRecordDao memberRemovalRecordDao;
	@Autowired
	private ForbiddenDao forbiddenDao;
	@Autowired
	private TemplateWebsiteLayoutDao templateWebsiteLayoutDao;
	@Autowired
	private DeptMemberDao deptMemberDao;
	@Autowired
	private DeptDao deptDao;
	
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
	public List<StaffVo> queryListStaff(Map<String, Object> params) {
		return memberDao.queryListStaff(params);
	}

	@Override
	public List<StaffVo> queryListStaffShift(Map<String, Object> params) {
		return memberDao.queryListStaffShift(params);
	}

	@Override
	public int queryTotalStaffCount(Integer superiorId) {
		return memberDao.queryTotalStaffCount(superiorId);
	}

	@Override
	public SessionMember login(String openid) {
		MemberEntity memberEntity = memberDao.queryObjectByOpenid(openid);
		SessionMember sessionMember = new SessionMember();
		if (memberEntity == null || BindStatusEnum.UNBIND.getCode().equals(memberEntity.getBindStatus())
				|| BindStatusEnum.NOT.getCode().equals(memberEntity.getBindStatus())) {
			//不是付费会员或者已被解绑
			CardEntity cardEntity = cardDao.queryObjectByOpenid(openid);
			if (cardEntity != null) {
				sessionMember.setNickname(cardEntity.getName());
				sessionMember.setPortrait(cardEntity.getPortrait());
			}
			sessionMember.setOpenid(openid);
			return sessionMember;
		}

		if (StringUtils.equals("freeze", memberEntity.getStatus())) {
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
		}

		Map<String, Object> map = tokenService.createToken(memberEntity.getId());
		sessionMember.setOpenid(memberEntity.getOpenid());
		sessionMember.setToken(map.get("token").toString());
		sessionMember.setMemberId(memberEntity.getId());
		if (StringUtils.isNotBlank(memberEntity.getMobile())) {
			sessionMember.setMobile(memberEntity.getMobile());
			sessionMember.setMobileCipher(memberEntity.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
		}
		sessionMember.setRole(memberEntity.getRole());
		sessionMember.setPortrait(memberEntity.getPortrait());
		sessionMember.setNickname(memberEntity.getNickname());
		sessionMember.setStartTime(memberEntity.getStartTime());
		sessionMember.setEndTime(memberEntity.getEndTime());
		sessionMember.setType(MemberTypeEnum.VIP.getCode());//vip付费会员
		sessionMember.setCertType(memberEntity.getCertType());
		if (StringUtils.equals(memberEntity.getRole(), MemberRoleEnum.BOSS.getCode())) {
			sessionMember.setSuperiorId(memberEntity.getId());
			sessionMember.setStaffMaxCount(memberEntity.getStaffMaxCount());
		} else {
			sessionMember.setSuperiorId(memberEntity.getSuperiorId());
			MemberEntity bossMember = memberDao.queryObject(memberEntity.getSuperiorId());
			sessionMember.setStaffMaxCount(bossMember.getStaffMaxCount());
		}

		long remainingDay = DateUtil.between(new Date(), sessionMember.getEndTime(), DateUnit.DAY, false);
		sessionMember.setRemainingDay(remainingDay);

		HashMap<String, Object> params = Maps.newHashMap();
		params.put("superiorId", sessionMember.getSuperiorId());
		params.put("authStatus", SystemConstant.T_STR);
		params.put("bindStatus", BindStatusEnum.BIND.getCode());
		//当前员工数量 包括自己
		int staffCount = memberDao.queryTotal(params) + 1;
		sessionMember.setStaffCount(staffCount);
		
		DeptMemberEntity deptMemberEntity = deptMemberDao.queryObjectByMemberId(sessionMember.getMemberId());
		if(deptMemberEntity != null) {
			sessionMember.setDeptId(deptMemberEntity.getDeptId());
			sessionMember.setPerm(deptMemberEntity.getPerm());
			DeptEntity deptEntity = deptDao.queryObject(deptMemberEntity.getDeptId());
			if(deptEntity != null) {
				sessionMember.setDeptName(deptEntity.getName());
			}
		}
		return sessionMember;
	}

	@Override
	@Transactional
	public void addStaff(Integer superiorId, String openid, String role) {

		if (StringUtils.equals(MemberRoleEnum.ADMIN.getCode(), role)
				|| StringUtils.equals(MemberRoleEnum.STAFF.getCode(), role)) {

			MemberEntity superiorMember = memberDao.queryObject(superiorId);
			Assert.isNullApi(superiorMember, "该团队不存在");
			if ("freeze".equals(superiorMember.getStatus())) {
				throw new ApiException("该团队已被冻结");
			}
			if (superiorMember.getEndTime().before(new Date())) {
				throw new ApiException("该团队已到期", 10003);
			}
			int staffCount = memberDao.queryTotalStaffCount(superiorId) + 1;//包括自己
			if (superiorMember.getStaffMaxCount() - staffCount <= 0) {
				throw new ApiException("该团长成员已满");
			}

			WxUserEntity wxUserEntity = wxUserDao.queryByOpenId(openid);
			Assert.isNullApi(wxUserEntity, "该用户不存在");
			if (StringUtils.isBlank(wxUserEntity.getNickName())) {
				throw new ApiException("该用户未授权信息");
			}

			MemberEntity memberEntityDB = memberDao.queryObjectByOpenid(openid);
			if (memberEntityDB != null
					&& StringUtils.equals(BindStatusEnum.BIND.getCode(), memberEntityDB.getBindStatus())) {
				throw new ApiException("该用户已经加入其他团队");
			}

			//处理微信用户头像
			String avatarurl = null;
			try {
				avatarurl = wxUserEntity.getAvatarUrl().replace("https", "http");
				String filePath = SystemConstant.TMP_DIR + IdUtil.fastSimpleUUID() + ".png";
				File avatarFile = ProjectUtils.downloadFile(avatarurl, filePath);
				String key = ProjectUtils.uploadCosFile(cosClient, avatarFile);
				avatarurl = imagePrefixUrl.concat(key);
				// 删除临时头像文件
				if (avatarFile != null) {
					avatarFile.delete();
				}
			} catch (Exception e) {
				log.error("===下载微信头像异常===", e);
				avatarurl = wxUserEntity.getAvatarUrl();
			}
			MemberEntity staffMember = new MemberEntity();
			staffMember.setAuthStatus(SystemConstant.F_STR)//未授权
					.setBindStatus(BindStatusEnum.NOT.getCode())//未绑定
					.setCertType(superiorMember.getCertType()).setCtime(new Date())
					.setEndTime(superiorMember.getEndTime())
					.setGender("1".equals(wxUserEntity.getGender()) ? "man" : "woman").setIsDel(SystemConstant.F_STR)
					.setNickname(wxUserEntity.getNickName()).setRealName(wxUserEntity.getNickName()).setOpenid(openid)
					.setPortrait(avatarurl).setRole(role).setStartTime(superiorMember.getStartTime())
					.setCompany(superiorMember.getCompany()).setStatus("normal").setSuperiorId(superiorId)
					.setType(MemberTypeEnum.VIP.getCode());//付费会员

			//更新
			/*if (memberEntityDB != null) {
				staffMember.setId(memberEntityDB.getId());
				memberDao.update(staffMember);
			} else {
				memberDao.save(staffMember);
			}*/
			
			//新增
			memberDao.save(staffMember);

			//处理员工名片
			CardEntity superiorEntity = cardDao.queryObjectByOpenid(superiorMember.getOpenid());
			Assert.isNullApi(superiorEntity, "团队名片不存在");
			CardEntity staffCard = cardDao.queryObjectByOpenid(openid);
			if (staffCard == null) {
				staffCard = new CardEntity();
				staffCard.setCompany(superiorEntity.getCompany());
				staffCard.setCtime(new Date());
				staffCard.setGender(staffMember.getGender());
				staffCard.setIsDel(SystemConstant.F_STR);
				staffCard.setMemberId(staffMember.getId());
				staffCard.setMobile(staffMember.getMobile());
				staffCard.setName(wxUserEntity.getNickName());
				staffCard.setOpenid(openid);
				staffCard.setPortrait(avatarurl);
				staffCard.setPosition(SystemConstant.DEFAULT_CARD_POSITION);
				staffCard.setUtime(new Date());
				cardDao.save(staffCard);
			} else {
				staffCard.setCompany(superiorEntity.getCompany());
				staffCard.setGender(staffMember.getGender());
				staffCard.setMemberId(staffMember.getId());
				staffCard.setMobile(staffMember.getMobile());
				staffCard.setName(wxUserEntity.getNickName());
				staffCard.setOpenid(openid);
				staffCard.setPortrait(avatarurl);
				staffCard.setUtime(new Date());
				cardDao.update(staffCard);
			}

		} else {
			throw new ApiException("只能添加管理员或者普通员工");
		}
	}

	@Override
	@Transactional
	public void shift(Integer sourceMemberId, Integer targetMemberId, SessionMember sessionMember) {

		if (sourceMemberId.equals(targetMemberId)) {
			throw new ApiException("不能迁移给同一个人");
		}
		
		MemberEntity sourceMember = memberDao.queryObject(sourceMemberId);
		Assert.isNullApi(sourceMember, "源会员不存在");
		MemberEntity targetMember = memberDao.queryObject(targetMemberId);
		if (targetMember.getSuperiorId().equals(0)) {
			//迁移到老板
			targetMember.setSuperiorId(targetMember.getId());
		}
		if (sourceMember.getSuperiorId().equals(0)) {
			sourceMember.setSuperiorId(sourceMember.getId());
		}
		Assert.isNullApi(targetMember, "目标会员不存在");
		if (!sourceMember.getSuperiorId().equals(sessionMember.getSuperiorId())
				|| !targetMember.getSuperiorId().equals(sessionMember.getSuperiorId())) {
			throw new ApiException("非法操作");
		}

		//迁移数据
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("sourceMemberId", sourceMemberId);
		params.put("targetMemberId", targetMemberId);
		for (String tableName : SystemConstant.REMOVAL_TABLE) {
			params.put("tableName", tableName);
			if (StringUtils.equals("tb_card", tableName) 
					|| StringUtils.equals("tb_card_holder", tableName)
					|| StringUtils.equals("tb_apply", tableName)) {
				
				params.put("sourceOpenid", sourceMember.getOpenid());
				params.put("targetOpenid", targetMember.getOpenid());
				int updateMemberIdAndOpenid = updateMemberIdAndOpenid(params);
				log.info("迁移表:{},完成数量:{}", tableName, updateMemberIdAndOpenid);
			} else {
				int updateMemberId = updateMemberId(params);
				log.info("迁移表:{},完成数量:{}", tableName, updateMemberId);
			}
		}

		//迁移记录
		MemberRemovalRecordEntity recordEntity = new MemberRemovalRecordEntity();
		recordEntity.setCtime(new Date());
		recordEntity.setOperationMember(sessionMember.getMemberId());
		recordEntity.setSourceMember(sourceMemberId);
		recordEntity.setTargetMember(targetMemberId);
		memberRemovalRecordDao.save(recordEntity);

	}

	@Override
	@Transactional
	public void unbind(Integer targetMemberId, Integer operationMemberId) {
		if (targetMemberId.equals(operationMemberId)) {
			throw new ApiException("不能解绑自己", 500);
		}
		MemberEntity temp = new MemberEntity();
		temp.setId(targetMemberId);
		temp.setBindStatus(BindStatusEnum.UNBIND.getCode());
		temp.setCertType(CertTypeEnum.UNKNOWN.getCode());
		temp.setAuthStatus(SystemConstant.F_STR);
		temp.setType(MemberTypeEnum.COMMON.getCode());
		memberDao.update(temp);
		//查询已有客户
		HashMap<String, Object> params = Maps.newHashMap();
		params.put("memberId", targetMemberId);
		List<ClienteleEntity> clienteleList = clienteleDao.queryList(params);
		if (CollectionUtil.isNotEmpty(clienteleList)) {
			ArrayList<ClienteleAllotRecordEntity> recordList = Lists.newArrayList();
			ClienteleAllotRecordEntity record = null;
			for (ClienteleEntity clientele : clienteleList) {
				record = new ClienteleAllotRecordEntity();
				record.setClienteleId(clientele.getId());
				record.setOperationMember(operationMemberId);
				record.setSourceMember(targetMemberId);
				record.setTargetMember(0);
				recordList.add(record);
			}
			clienteleAllotRecordDao.insertBatch(recordList);
		}

		//清除客户负责人ID 重置为0
		clienteleDao.updateClearMemberId(targetMemberId);
		//删除官网布局
		templateWebsiteLayoutDao.deleteByMemberId(targetMemberId);
		//删除该员工所在部门
		deptMemberDao.deleteByMemberId(targetMemberId);
	}

	@Override
	public int updateBySuperiorId(MemberEntity memberEntity) {
		return memberDao.updateBySuperiorId(memberEntity);
	}

	@Override
	public MemberEntity queryObjectByCode(String code) {
		return memberDao.queryObjectByCode(code);
	}

	@Override
	@Transactional
	public SessionMember activate(String openid, String mobile, String acode) {
		MemberEntity currentMember = memberDao.queryObjectByOpenid(openid);
		if (currentMember != null && BindStatusEnum.BIND.getCode().equals(currentMember.getBindStatus())) {
			throw new ApiException("您已是VIP会员无法领取");
		}

		MemberEntity activateMember = memberDao.queryObjectByCode(acode);
		Assert.isNullApi(activateMember, "激活码无效");
		if (StringUtils.isNotBlank(activateMember.getOpenid())) {
			throw new ApiException("激活码无效");
		}

		if (!StringUtils.equals(mobile, activateMember.getMobile())) {
			throw new ApiException("该手机号无权领取");
		}

		WxUserEntity wxUserEntity = wxUserDao.queryByOpenId(openid);
		
		MemberEntity temp = new MemberEntity();
		temp.setId(activateMember.getId());
		temp.setMobile(mobile);
		temp.setOpenid(openid);
		temp.setCompany(SystemConstant.DEFAULT_CARD_COMPANY);
		if (wxUserEntity == null || StringUtils.isBlank(wxUserEntity.getNickName())) {
			temp.setPortrait(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
			temp.setNickname(SystemConstant.DEFAULT_CARD_NAME);
			temp.setRealName(SystemConstant.DEFAULT_CARD_NAME);
		} else {
			temp.setPortrait(wxUserEntity.getAvatarUrl());
			temp.setNickname(wxUserEntity.getNickName());
			temp.setRealName(wxUserEntity.getNickName());
		}
		
		memberDao.update(temp);

		SessionMember sessionMember = new SessionMember();
		Map<String, Object> map = tokenService.createToken(activateMember.getId());
		sessionMember.setOpenid(temp.getOpenid());
		sessionMember.setToken(map.get("token").toString());
		sessionMember.setMemberId(temp.getId());
		if (StringUtils.isNotBlank(temp.getMobile())) {
			sessionMember.setMobile(temp.getMobile());
			sessionMember.setMobileCipher(temp.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
		}
		sessionMember.setRole(activateMember.getRole());
		sessionMember.setPortrait(temp.getPortrait());
		sessionMember.setNickname(temp.getNickname());
		sessionMember.setStartTime(activateMember.getStartTime());
		sessionMember.setEndTime(activateMember.getEndTime());
		sessionMember.setType(MemberTypeEnum.VIP.getCode());//vip付费会员
		sessionMember.setCertType(activateMember.getCertType());
		if (StringUtils.equals(activateMember.getRole(), MemberRoleEnum.BOSS.getCode())) {
			sessionMember.setSuperiorId(activateMember.getId());
			sessionMember.setStaffMaxCount(activateMember.getStaffMaxCount());
		} else {
			sessionMember.setSuperiorId(activateMember.getSuperiorId());
		}
		
		long remainingDay = DateUtil.between(new Date(), sessionMember.getEndTime(), DateUnit.DAY, false);
		sessionMember.setRemainingDay(remainingDay);
		return sessionMember;
	}

	@Override
	public List<MemberEntityVo> queryListVO(Map<String, Object> map) {
		return memberDao.queryListVO(map);
	}

	@Override
	public List<MemberEntityVo> queryListBySuperiorId(Map<String, Object> map) {
		return memberDao.queryListBySuperiorId(map);
	}

	@Override
	public List<MemberEntityVo> queryListByCompany(Map<String, Object> map) {
		return memberDao.queryListByCompany(map);
	}

	@Override
	public int updateMemberId(Map<String, Object> map) {
		return memberDao.updateMemberId(map);
	}

	@Override
	public int updateMemberIdAndOpenid(Map<String, Object> map) {
		return memberDao.updateMemberIdAndOpenid(map);
	}

	@Override
	public Boolean saveVipMember(MemberEntity member) {
		member.setCtime(new Date());
		member.setRole("boss");
		member.setIsDel("f");
		member.setStatus("normal");
		member.setSuperiorId(0);
		member.setCertType("unknown");
		member.setType("vip");
		member.setAuthStatus("t");
		member.setCode(UUID.randomUUID().toString());
		member.setBindStatus("bind");
		int number = memberDao.saveVipMember(member);
		return number > 0;
	}

	@Override
	public Boolean forbiddenMember(Integer id) {
		return memberDao.forbiddenMember(id) > 0;
	}

	@Override
	public Boolean openMember(Integer id) {
		return memberDao.openMember(id) > 0;
	}

	@Override
	public MemberEntity queryObjectByMobile(String mobile) {
		return memberDao.queryObjectByMobile(mobile);
	}

	@Override
	@Transactional
	public void enableVip(String mobile) {
		MemberEntity memberEntity = memberDao.queryObjectByMobile(mobile);
		if (memberEntity != null && BindStatusEnum.BIND.getCode().equals(memberEntity.getBindStatus())) {
			throw new ApiException(mobile + "已开通，不能重复开通");
		}
		
		Date now = new Date();
		if(memberEntity == null) {
			memberEntity = new MemberEntity();
			memberEntity.setCtime(now)
			.setRole(MemberRoleEnum.BOSS.getCode())
			.setIsDel(SystemConstant.F_STR)
			.setStatus("normal")
			.setSuperiorId(0)
			.setStartTime(now)
			.setEndTime(DateUtil.offset(now, DateField.DAY_OF_YEAR, 365))//默认365天
			.setMobile(mobile)
			.setCertType(CertTypeEnum.UNKNOWN.getCode())
			.setType(MemberTypeEnum.VIP.getCode())
			.setAuthStatus(SystemConstant.T_STR)
			.setBindStatus(BindStatusEnum.BIND.getCode())
			.setCode(UUID.randomUUID().toString())
			.setCompany(SystemConstant.DEFAULT_CARD_COMPANY)
			.setStaffMaxCount(5);// 默认5人
			memberDao.save(memberEntity);
		} else {
			 memberEntity.setRole(MemberRoleEnum.BOSS.getCode())
			.setIsDel(SystemConstant.F_STR)
			.setStatus("normal")
			.setSuperiorId(0)
			.setStartTime(now)
			.setEndTime(DateUtil.offset(now, DateField.DAY_OF_YEAR, 365))//默认365天
			.setMobile(mobile)
			.setCertType(CertTypeEnum.UNKNOWN.getCode())
			.setType(MemberTypeEnum.VIP.getCode())
			.setAuthStatus(SystemConstant.T_STR)
			.setBindStatus(BindStatusEnum.BIND.getCode())
			.setCode(UUID.randomUUID().toString())
			.setStaffMaxCount(5);// 默认5人
			 memberDao.update(memberEntity);
		}
		
	}

	@Override
	public List<StaffVo> queryListDeptStaff(Map<String, Object> params) {
		return memberDao.queryListDeptStaff(params);
	}

	@Override
	public List<StaffVo> queryListDeptStaffOff(Map<String, Object> params) {
		return memberDao.queryListDeptStaffOff(params);
	}

	@Override
	@Transactional
	public void changeRole(MemberEntity memberEntity) {
		if(StringUtils.equals(MemberRoleEnum.ADMIN.getCode(), memberEntity.getRole())) {
			deptMemberDao.deleteByMemberId(memberEntity.getId());
		}
		memberDao.update(memberEntity);
	}

}
