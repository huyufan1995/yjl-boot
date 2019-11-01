package io.renren.cms.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.binarywang.wxpay.service.WxPayService;
import com.qcloud.cos.COSClient;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.MemberEntityVo;
import io.renren.cms.entity.ForbiddenMemberEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.CardService;
import io.renren.cms.service.ForbiddenService;
import io.renren.cms.service.MemberService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.renren.utils.PageUtils;
import io.renren.utils.ProjectUtils;
import io.renren.utils.Query;
import io.renren.utils.R;
import lombok.extern.slf4j.Slf4j;


/**
 * 会员
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-08-21 13:51:48
 */
@RestController
@RequestMapping("member")
@Slf4j
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private ForbiddenService forbiddenService;
	@Autowired
	private COSClient cosClient;

	@Autowired
	private WxPayService wxPayService;

	@Autowired
	private CardService cardService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("member:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        //封装上级id
		List<MemberEntityVo> memberList = memberService.queryListVO(query);
		/*List<MemberEntityVo> memberEntityVoList = new ArrayList<>();
		memberList.forEach(m ->{
			Integer id = m.getSuperiorId();
			MemberEntityVo vo = new MemberEntityVo();
			BeanUtils.copyProperties(m,vo);
			memberList.forEach(m1 ->{
				if(m1.getId().equals(id)){
					vo.setMemberBossName(m1.getNickname());
				}
			});
			memberEntityVoList.add(vo);
		});*/
		int total = memberService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(memberList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 */
	@RequestMapping("/enterpriseList")
	//@RequiresPermissions("member:list")
	public R enterpriseList(@RequestParam Map<String, Object> params){
		params.put("certType","enterprise");
		Query query = new Query(params);
		//封装上级id
		List<MemberEntityVo> memberList = memberService.queryListVO(query);
		/*List<MemberEntityVo> memberEntityVoList = new ArrayList<>();
		memberList.forEach(m ->{
			Integer id = m.getSuperiorId();
			MemberEntityVo vo = new MemberEntityVo();
			BeanUtils.copyProperties(m,vo);
			memberList.forEach(m1 ->{
				if(m1.getId().equals(id)){
					vo.setMemberBossName(m1.getNickname());
				}
			});
			memberEntityVoList.add(vo);
		});*/
		int total = memberService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(memberList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}


	@RequestMapping("/superiorId")
	//@RequiresPermissions("member:list")
	public R list(@RequestParam String superiorId){
		HashMap<String, Object> params = new HashMap();
		params.put("superiorId",superiorId);
		Query query = new Query(params);
		//封装上级id
		List<MemberEntityVo> memberList = memberService.queryListBySuperiorId(query);
		/*List<MemberEntityVo> memberEntityVoList = new ArrayList<>();
		memberList.forEach(m ->{
			Integer id = m.getSuperiorId();
			MemberEntityVo vo = new MemberEntityVo();
			BeanUtils.copyProperties(m,vo);
			memberList.forEach(m1 ->{
				if(m1.getId().equals(id)){
					vo.setMemberBossName(m1.getNickname());
				}
			});
			memberEntityVoList.add(vo);
		});*/
		int total = memberService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(memberList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 通过公司查询用户列表
	 * @param id
	 * @return
	 */
	@RequestMapping("/company")
	//@RequiresPermissions("member:list")
	public R companyList(@RequestParam String company,@RequestParam Map<String, Object> params){
		//HashMap<String, Object> params = new HashMap();
		try{
			company=new String(company.getBytes("iso-8859-1"),"utf-8");
		}catch (Exception e){
			e.printStackTrace();
		}
		params.put("company",company);
		Query query = new Query(params);
		List<MemberEntityVo> memberList = memberService.queryListByCompany(query);
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


	@RequestMapping("/memberVipImg/{id}")
	public void informationImg(@PathVariable("id") Integer id, HttpServletResponse response){
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		MemberEntity memberEntity = memberService.queryObject(id);
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_ACTIVATE_VIP, memberEntity.getCode()), 280, false, null, false);
			String key = ProjectUtils.uploadCosFile(cosClient, qrcodeFile);
			System.err.println(yykjProperties.getImagePrefixUrl().concat(key));
			//读取指定路径下面的文件
			InputStream in = new FileInputStream(qrcodeFile);
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			//创建存放文件内容的数组
			byte[] buff =new byte[1024];
			//所读取的内容使用n来接收
			int n;
			//当没有读取完时,继续读取,循环
			while((n=in.read(buff))!=-1){
				//将字节数组的数据全部写入到输出流中
				outputStream.write(buff,0,n);
			}
			//强制将缓存区的数据进行输出
			outputStream.flush();
			//关流
			outputStream.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===生成会员邀请二维码异常：{}", e.getMessage());
		}
	}
    /**
     * 开通VIp
     */
    @RequestMapping("/saveVipMember")
    //@RequiresPermissions("member:save")
    public R saveVipMember(@RequestBody MemberEntity member,HttpServletResponse response){
        memberService.saveVipMember(member);
        return R.ok();
    }

	/**
	 * 增加子账号数量
	 */
	@RequestMapping("/updateVipMebmerStaff")
	//@RequiresPermissions("member:save")
	public R saveVipMember(@RequestBody MemberEntity member){
		Integer staffMaxCount = memberService.queryObject(member.getId()).getStaffMaxCount();
		member.setStaffMaxCount(member.getStaffMaxCount()+staffMaxCount);
		memberService.update(member);
		return R.ok();
	}

	/**
	 * VIP会员续期
	 */
	@RequestMapping("/renewalMebmer")
	//@RequiresPermissions("member:save")
	public R renewalMebmer(@RequestBody MemberEntity member){
		memberService.update(member);
		return R.ok();
	}

	/**
	 * 封禁用户
	 */
	@RequestMapping("/forbiddenMember")
	//@RequiresPermissions("member:save")
	@Transactional
	public R renewalMebmer(@RequestBody ForbiddenMemberEntity forbiddenMemberEntity){
		MemberEntity memberEntity = memberService.queryObjectByOpenid(forbiddenMemberEntity.getOpenId());
        memberService.update(memberEntity.setStatus("freeze"));
		Integer superiorId = memberEntity.getSuperiorId();
		if(superiorId == 0){
            memberService.forbiddenMember(memberEntity.getId());
		}
		forbiddenMemberEntity.setCreateDate(new Date());
        forbiddenMemberEntity.setSuperiorId(memberEntity.getId());
        forbiddenService.save(forbiddenMemberEntity);
		return R.ok();
	}

	/**
	 * 解禁用户
	 */
	@RequestMapping("/openMember")
	//@RequiresPermissions("member:save")
	public R openMember(@RequestParam String id){
		MemberEntity memberEntity = memberService.queryObject(Integer.parseInt(id));
		memberService.update(memberEntity.setStatus("normal"));
		if(memberEntity.getSuperiorId()==0){
			memberService.openMember(memberEntity.getId());
			forbiddenService.delete(memberEntity.getSuperiorId());
		}
		return R.ok();
	}
}
