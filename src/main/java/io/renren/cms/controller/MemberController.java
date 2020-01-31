package io.renren.cms.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import io.renren.api.constant.SystemConstant;
import io.renren.cms.entity.ForbiddenMemberEntity;
import io.renren.cms.entity.InformationTypeEntity;
import io.renren.cms.service.ForbiddenService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.AuditStatusEnum;
import io.renren.enums.MemberTypeEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;


/**
 * 会员
 * 
 * @author moran
 * @email ${email}
 * @date 2019-11-15 14:20:00
 */
@Slf4j
@RestController
@RequestMapping("member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@Autowired
	private YykjProperties yykjProperties;

	@Autowired
	private COSClient cosClient;

	@Autowired
	private ForbiddenService forbiddenService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("member:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<MemberEntity> memberList = memberService.queryListVO(query);
		int total = memberService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(memberList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("member:info")
	public R info(@PathVariable("id") Integer id){
		MemberEntity member = memberService.queryObject(id);
		
		return R.ok().put("member", member);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("member:save")
	public R save(@RequestBody MemberEntity member){
		memberService.save(member);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("member:update")
	public R update(@RequestBody MemberEntity member){
		if("女".equals(member.getGender())){
			member.setGender("2");
		}else{
			member.setGender("1");
		}
		memberService.update(member);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("member:delete")
	public R delete(@RequestBody Integer[] ids){
		memberService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("member:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		memberService.logicDel(id);
		return R.ok();
	}

	/**
	 * 更新用户认证授权状态
	 */
	@RequestMapping("/updateType")
	//@RequiresPermissions("member:save")
	public R updateType(@RequestBody MemberEntity member){
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setId(member.getId());
		memberEntity.setAuditStatus(AuditStatusEnum.PASS.getCode());
		memberEntity.setType(MemberTypeEnum.VIP.getCode());
		String id = System.currentTimeMillis() + "";
		id = id.substring(0, 6);
		memberEntity.setCode(id + memberEntity.getId());
		memberEntity.setAuditMsg(" ");
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_Member_DETAIL, memberEntity.getCode()), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			System.err.println(yykjProperties.getImagePrefixUrl().concat(key));
			memberEntity.setQrCode(yykjProperties.getImagePrefixUrl().concat(key));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===生成会员二维码异常：{}", e.getMessage());
		}
		memberService.update(memberEntity);

		return R.ok();
	}


	/**
	 * 更新用户认证授权状态
	 */
	@RequestMapping("/updateTypeNopass")
	//@RequiresPermissions("member:save")
	public R updateTypeNoPass(@RequestBody MemberEntity member){
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setId(member.getId());
		memberEntity.setAuditStatus(AuditStatusEnum.REJECT.getCode());
		memberEntity.setAuditMsg(member.getAuditMsg());
		memberService.update(memberEntity);
		return R.ok();
	}
	/**
	 * 查询所有会员  设置会员banner用的方法
	 */
	@RequestMapping("/queryAll")
	//@RequiresPermissions("informationtype:delete")
	public R queryAll(){
		Map<String,Object> params = new HashMap<>(1);
		params.put("type","vip");
		List<MemberEntity> memberEntities = memberService.queryListByMemberBanner(params);

		return R.ok().put("memberList", memberEntities);
	}

	/**
	 * 封禁用户
	 */
	@RequestMapping("/forbiddenMember")
	//@RequiresPermissions("member:save")
	@Transactional
	public R renewalMebmer(@RequestBody ForbiddenMemberEntity forbiddenMemberEntity){
		MemberEntity memberEntity = memberService.queryObjectByOpenid(forbiddenMemberEntity.getOpenId());
		memberEntity.setStatus("freeze");
		memberService.update(memberEntity);
		forbiddenMemberEntity.setCreateDate(new Date());
		forbiddenService.save(forbiddenMemberEntity);
		return R.ok();
	}

	/**
	 * 解禁用户
	 */
	@RequestMapping("/openMember")
	@Transactional
	//@RequiresPermissions("member:save")
	public R openMember(@RequestParam String id){
		MemberEntity memberEntity = memberService.queryObject(Integer.parseInt(id));
		memberEntity.setStatus("normal");
		memberService.update(memberEntity);
		forbiddenService.deleteByOpenId(memberEntity.getOpenid());
		return R.ok();
	}


	/**
	 * 查询所有会员  设置核销员用的方法
	 */
	@RequestMapping("/queryListAll")
	//@RequiresPermissions("informationtype:delete")
	public R queryListAll(){
		List<MemberEntity> memberEntities = memberService.queryList(null);

		return R.ok().put("memberList", memberEntities);
	}

	/**
	 * 取消VIP logo
	 */
	@RequestMapping("/removeVipLogo/{id}")
	//@RequiresPermissions("member:logicDel")
	public R removeVipLogo(@PathVariable("id") Integer id) {
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setId(id);
		memberEntity.setShowVip("f");
		memberService.update(memberEntity);
		return R.ok();
	}

	/**
	 * 增加VIP logo
	 */
	@RequestMapping("/addVipLogo/{id}")
	//@RequiresPermissions("member:logicDel")
	public R addVipLogo(@PathVariable("id") Integer id) {
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setId(id);
		memberEntity.setShowVip("t");
		memberService.update(memberEntity);
		return R.ok();
	}
}
