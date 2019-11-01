package io.renren.cms.controller;

import java.util.List;
import java.util.Map;

import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CertificateEntity;
import io.renren.cms.service.CertificateService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

/**
 * 认证
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:47
 */
@RestController
@RequestMapping("certificate")
public class CertificateController {

	@Autowired
	private CertificateService certificateService;
	@Autowired
	private MemberService memberService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("certificate:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<CertificateEntity> certificateList = certificateService.queryList(query);
		int total = certificateService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(certificateList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("certificate:info")
	public R info(@PathVariable("id") Integer id) {
		CertificateEntity certificate = certificateService.queryObject(id);

		return R.ok().put("certificate", certificate);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("certificate:save")
	public R save(@RequestBody CertificateEntity certificate) {
		Integer memberId = certificate.getMemberId();
		if(certificate.getStatus().equals("reject")){
			certificate.setType("unknown");
		}
		if(certificate.getStatus().equals("pass")){
			certificate.setType("enterprise");
		}
		String type = certificate.getType();
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setSuperiorId(memberId);
		memberEntity.setCertType(type);
		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setCertType(type);
		memberEntity1.setId(memberId);
		memberService.update(memberEntity1);
		memberService.updateBySuperiorId(memberEntity);
		certificateService.save(certificate);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("certificate:update")
	public R update(@RequestBody CertificateEntity certificate) {
		Integer memberId = certificate.getMemberId();
		if(certificate.getStatus().equals("reject")){
			certificate.setType("unknown");
		}
		if(certificate.getStatus().equals("pass")){
			certificate.setType("enterprise");
		}
		String type = certificate.getType();
		MemberEntity memberEntity = new MemberEntity();
		memberEntity.setSuperiorId(memberId);
		memberEntity.setCertType(type);
		memberService.updateBySuperiorId(memberEntity);
		MemberEntity memberEntity1 = new MemberEntity();
		memberEntity1.setCertType(type);
		memberEntity1.setId(memberId);
		memberService.update(memberEntity1);
		certificateService.update(certificate);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("certificate:delete")
	public R delete(@RequestBody Integer[] ids) {
		certificateService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("certificate:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		certificateService.logicDel(id);
		return R.ok();
	}

}
