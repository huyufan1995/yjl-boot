package io.renren.cms.controller;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.qcloud.cos.COSClient;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApplyEntityVo;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.cms.service.ForbiddenService;
import io.renren.config.WxMaConfiguration;
import io.renren.entity.SysConfigEntity;
import io.renren.properties.YykjProperties;
import io.renren.service.SysConfigService;
import io.renren.utils.ProjectUtils;
import io.renren.utils.annotation.SysLog;
import io.renren.utils.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.service.ApplyService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 报名
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-18 11:37:44
 */
@RestController
@RequestMapping("apply")
public class ApplyController {
	@Autowired
	private ApplyService applyService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private YykjProperties yykjProperties;
	@Autowired
	private COSClient cosClient;

	@Autowired
	private ApplyRecordService applyRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("apply:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
		List<ApplyEntityVo> applyList = applyService.queryListVo(query);
		int total = applyService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(applyList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}


	/**
	 * 报名活动数据列表
	 */
	@RequestMapping("/applyData")
	//@RequiresPermissions("apply:list")
	public R applyData(@RequestParam String id){
		List<ApplyRecordEntity> applyRecordEntities = applyRecordService.applyRecordListByApplyId(id);
        JSONArray dataArray = JSONUtil.parseArray(applyRecordEntities);
        //JSONArray dataArray = JSONUtil.parseArray(applyRecordEntity.getItemDetail());
		/*List<ApplyEntityVo> applyList = applyService.queryListVo(query);
		int total = applyService.queryTotal(query);*/
		PageUtils pageUtil = new PageUtils(applyRecordEntities, dataArray.size(), 5, 1);
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("apply:info")
	public R info(@PathVariable("id") Integer id){
		ApplyEntity apply = applyService.queryObject(id);
		
		return R.ok().put("apply", apply);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("apply:save")
	public R save(@RequestBody ApplyEntity apply){
		applyService.save(apply);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("apply:update")
	public R update(@RequestBody ApplyEntity apply){
		applyService.update(apply);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("apply:delete")
	public R delete(@RequestBody Integer[] ids){
		applyService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("apply:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		applyService.logicDel(id);
		return R.ok();
	}


	@RequestMapping("/applyImg/{id}")
	public void applyImg(@PathVariable("id") Integer id, HttpServletResponse response){
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		ApplyEntity applyEntity = applyService.queryObject(id);
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_ACTIVATE_VIP, applyEntity.getId()), 280, false, null, false);
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
		}
	}


	/**
	 * 查询活动报表标签数据
	 */
	@RequestMapping("/applyTag")
	public R applyTag(){
		String value = sysConfigService.getValue(SystemConstant.KEY_DEFAULT_APPLY_ITEM, "");
		return R.ok(value);
	}

	/**
	 * 保存活动标签配置数据
	 */
	@RequestMapping("/saveApplyTag")
	public R saveApplyTag(@RequestBody String params){
		sysConfigService.updateValueByKey(SystemConstant.KEY_DEFAULT_APPLY_ITEM,params);
		return R.ok();
	}

}
