package io.renren.cms.controller;

import java.io.*;
import java.util.List;
import java.util.Map;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.CaseEntityVo;
import io.renren.cms.entity.MemberEntity;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import io.renren.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.CaseEntity;
import io.renren.cms.service.CaseService;
import io.renren.utils.PageUtils;
import io.renren.utils.Query;
import io.renren.utils.R;

import javax.servlet.http.HttpServletResponse;

/**
 * 案例
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-10-09 11:14:10
 */
@RestController
@RequestMapping("casus")
public class CaseController {
	@Autowired
	private CaseService caseService;

	@Autowired
	private COSClient cosClient;

	@Autowired
	private YykjProperties yykjProperties;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("case:list")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);

		List<CaseEntityVo> caseList = caseService.queryListVo(query);
		int total = caseService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(caseList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("case:info")
	public R info(@PathVariable("id") Integer id) {
		CaseEntity caseEntity = caseService.queryObject(id);

		return R.ok().put("case", caseEntity);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("case:save")
	public R save(@RequestBody CaseEntity caseEntity) {
		caseService.save(caseEntity);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("case:update")
	public R update(@RequestBody CaseEntity caseEntity) {
		caseService.update(caseEntity);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("case:delete")
	public R delete(@RequestBody Integer[] ids) {
		caseService.deleteBatch(ids);

		return R.ok();
	}

	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("case:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		caseService.logicDel(id);
		return R.ok();
	}

	@RequestMapping("/infoImg/{id}")
	public void infoImg(@PathVariable("id") Integer id, HttpServletResponse response){
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");
		final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
		CaseEntity caseEntity = caseService.queryObject(id);
		try {
			File qrcodeFile = wxMaService.getQrcodeService().createWxaCode(
					StrUtil.format(SystemConstant.APP_PAGE_PATH_CASE_DETAIL,caseEntity.getId(),caseEntity.getMemberId()), 280, false, null, false);
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

}
