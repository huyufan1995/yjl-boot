package io.renren.api.vo;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import io.renren.cms.entity.MemberRemovalRecordEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 员工 下属
 * @author yujia
 *
 */
@Getter
@Setter
public class StaffVo {

	/**  */
	private Integer id;
	/** 头像 */
	private String portrait;
	/** 昵称 */
	private String nickname;
	/** 性别 man woman */
	private String gender;
	/**  */
	private Date ctime;
	/** 角色 boss admin staff */
	private String role;
	/** 上级ID */
	private Integer superiorId;
	/** 姓名 */
	private String realName;
	/** 会员开始时间 */
	private Date startTime;
	/** 会员结束时间 */
	private Date endTime;
	/** 手机号 */
	private String mobile;
	/** 微信用户ID */
	private String openid;
	/** 认证类型 unknown personage enterprise */
	private String certType;
	/** 会员类型 common vip */
	private String type;
	/** 授权状态 t or f */
	private String authStatus;
	/** 迁移目标会员ID */
	//	private Integer shiftMemberId;
	/** 迁移目标会员姓名 */
	//	private String shiftMemberName;
	/** 迁移时间 */
	//	private Date shiftTime;
	/** 绑定状态 not未绑定 unbind已解绑 bind已经绑定 */
	private String bindStatus;

	/** 迁移记录 */
	private List<MemberRemovalRecordEntity> removalRecord = Lists.newArrayList();

	private Integer deptId;
	private String perm;
	/** 部门名称 */
	private String deptName;
	/** 加入部门时间 */
	private Date joinTime;

}
