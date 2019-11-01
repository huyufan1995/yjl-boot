package io.renren.cms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.TemplateVo;
import io.renren.api.vo.TemplateWebsiteEntityVo;
import io.renren.api.vo.box.*;
import io.renren.properties.YykjProperties;
import io.renren.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.cms.entity.TemplateWebsiteEntity;
import io.renren.cms.service.TemplateWebsiteService;


/**
 * 官网模板
 * 
 * @author yujia
 * @email yujiain2008@163.com
 * @date 2019-09-02 14:16:12
 */
@RestController
@RequestMapping("templatewebsite")
public class TemplateWebsiteController {
	@Autowired
	private TemplateWebsiteService templateWebsiteService;
	@Autowired
	private YykjProperties yykjProperties;

	@Autowired
	private RedisUtils redisUtils;

	@Value("${tencent.cos.imagePrefixUrl}")
	private String imagePrefixUrl;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("templatewebsite:list")
	public R list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);

		List<TemplateWebsiteEntity> templateWebsiteList = templateWebsiteService.queryList(query);
		/*templateWebsiteList.forEach(templateWebsiteEntity -> {
			templateWebsiteEntity.setExamplePath(yykjProperties.getVisitprefix()+templateWebsiteEntity.getExamplePath());
		});*/
		int total = templateWebsiteService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(templateWebsiteList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("templatewebsite:info")
	public R info(@PathVariable("id") Integer id){
		TemplateWebsiteEntity templateWebsite = templateWebsiteService.queryObject(id);
		String layout = templateWebsite.getLayout();
		TemplateVo t = JSONUtil.toBean(layout, TemplateVo.class);
		TemplateWebsiteEntityVo vo = new TemplateWebsiteEntityVo();
		vo.setIndexImgSrc(t.getIndex());
		vo.setPhoneImgSrc(t.getPhone());
		vo.setShareImgSrc(t.getShare());
		vo.setFontColor(templateWebsite.getFontColor());
		vo.setBox1MpImgSrc(t.getBox1().getMp_bg());
		vo.setBox1BgImgSrc(t.getBox1().getBg());
		vo.setBox2BgImgSrc(t.getBox2().getBg());
		vo.setBox2TopImgSrc(t.getBox2().getTop());
		vo.setBox3BgImgSrc(t.getBox3().getBg());
		vo.setBox3TopImgSrc(t.getBox3().getTop());
		vo.setBox4BgImgSrc(t.getBox4().getBg());
		vo.setBox5BgImgSrc(t.getBox5().getBg());
		vo.setBox5TopImgSrc(t.getBox5().getTop());
		vo.setBox6BgImgSrc(t.getBox6().getBg());
		vo.setBox7BgImgSrc(t.getBox7().getBg());
		vo.setBox7BoxImgSrc(t.getBox7().getBox());
		vo.setBox7BtnImgSrc(t.getBox7().getBtn());
		vo.setBox8BgImgSrc(t.getBox8().getBg());
		BeanUtils.copyProperties(templateWebsite,vo);
		return R.ok().put("templateWebsite", vo);
	}

	/**
	 * 发布微官网模版
	 */
	@RequestMapping("/releasetemplate/{id}")
	//@RequiresPermissions("templatewebsite:info")
	public R releasetemplate(@PathVariable("id") Integer id){
		TemplateWebsiteEntity templateWebsite = new TemplateWebsiteEntity();
		templateWebsite.setId(id);
		templateWebsite.setIsRelease("t");
		templateWebsiteService.update(templateWebsite);
		return R.ok();
	}

	/**
	 * 撤回微官网模版
	 */
	@RequestMapping("/revocationtemplate/{id}")
	//@RequiresPermissions("templatewebsite:info")
	public R revocationtemplate(@PathVariable("id") Integer id){
		TemplateWebsiteEntity templateWebsite = new TemplateWebsiteEntity();
		templateWebsite.setId(id);
		templateWebsite.setIsRelease("f");
		templateWebsiteService.update(templateWebsite);
		return R.ok();
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("templatewebsite:save")
	public R save(@RequestBody TemplateWebsiteEntityVo vo){
		TemplateVo templateVo = new TemplateVo();
		templateVo.setFont_color(vo.getFontColor());
		templateVo.setIndex(imagePrefixUrl.concat(redisUtils.get("indexImgSrc")));
		templateVo.setPhone(imagePrefixUrl.concat(redisUtils.get("phoneImgSrc")));
		templateVo.setShare(imagePrefixUrl.concat(redisUtils.get("shareImgSrc")));
		templateVo.setBox1(new Box1(imagePrefixUrl.concat(redisUtils.get("box1BgImgSrc")),imagePrefixUrl.concat(redisUtils.get("box1MpImgSrc"))));
		templateVo.setBox2(new Box2(imagePrefixUrl.concat(redisUtils.get("box2TopImgSrc")),imagePrefixUrl.concat(redisUtils.get("box2BgImgSrc"))));
		templateVo.setBox3(new Box3(imagePrefixUrl.concat(redisUtils.get("box3TopImgSrc")),imagePrefixUrl.concat(redisUtils.get("box3BgImgSrc"))));
		templateVo.setBox4(new Box4(imagePrefixUrl.concat(redisUtils.get("box4BgImgSrc"))));
		templateVo.setBox5(new Box5(imagePrefixUrl.concat(redisUtils.get("box5TopImgSrc")),imagePrefixUrl.concat(redisUtils.get("box5BgImgSrc"))));
		templateVo.setBox6(new Box6(imagePrefixUrl.concat(redisUtils.get("box6BgImgSrc"))));
		templateVo.setBox7(new Box7(imagePrefixUrl.concat(redisUtils.get("box7BgImgSrc")),imagePrefixUrl.concat(redisUtils.get("box7BoxImgSrc")),imagePrefixUrl.concat(redisUtils.get("box7BtnImgSrc"))));
		templateVo.setBox8(new Box8(imagePrefixUrl.concat(redisUtils.get("box8BgImgSrc"))));
		String layout = JSONUtil.toJsonStr(templateVo);
		TemplateWebsiteEntity templateWebsiteEntity = new TemplateWebsiteEntity();
		templateWebsiteEntity.setCtime(new Date());
		templateWebsiteEntity.setUtime(new Date());
		templateWebsiteEntity.setExamplePath(imagePrefixUrl.concat(redisUtils.get("exampleImgSrc")));
		templateWebsiteEntity.setUseCnt(0);
		templateWebsiteEntity.setUtime(new Date());
		templateWebsiteEntity.setIsDel("f");
		templateWebsiteEntity.setIsRelease("f");
		templateWebsiteEntity.setInitUseCnt(0);
		templateWebsiteEntity.setLayout(layout);
		templateWebsiteEntity.setMetadata(SystemConstant.DEFAULT_METADATA);
		templateWebsiteService.save(templateWebsiteEntity);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("templatewebsite:update")
	public R update(@RequestBody TemplateWebsiteEntityVo vo){
		TemplateVo templateVo = new TemplateVo();
		templateVo.setFont_color(vo.getFontColor());
		templateVo.setIndex(vo.getIndexImgSrc());
		templateVo.setPhone(vo.getPhoneImgSrc());
		templateVo.setShare(vo.getShareImgSrc());
		templateVo.setBox1(new Box1(vo.getBox1BgImgSrc(),vo.getBox1MpImgSrc()));
		templateVo.setBox2(new Box2(vo.getBox2TopImgSrc(),vo.getBox2BgImgSrc()));
		templateVo.setBox3(new Box3(vo.getBox3TopImgSrc(),vo.getBox3BgImgSrc()));
		templateVo.setBox4(new Box4(vo.getBox4BgImgSrc()));
		templateVo.setBox5(new Box5(vo.getBox5TopImgSrc(),vo.getBox5BgImgSrc()));
		templateVo.setBox6(new Box6(vo.getBox6BgImgSrc()));
		templateVo.setBox7(new Box7(vo.getBox7BgImgSrc(),vo.getBox7BoxImgSrc(),vo.getBox7BtnImgSrc()));
		templateVo.setBox8(new Box8(vo.getBox8BgImgSrc()));
		String layout = JSONUtil.toJsonStr(templateVo);
		TemplateWebsiteEntity templateWebsiteEntity = new TemplateWebsiteEntity();
		templateWebsiteEntity.setFontColor(vo.getFontColor());
		templateWebsiteEntity.setId(vo.getId());
		templateWebsiteEntity.setExamplePath(vo.getExamplePath());
		templateWebsiteEntity.setUseCnt(0);
		templateWebsiteEntity.setUtime(new Date());
		templateWebsiteEntity.setIsDel("f");
		templateWebsiteEntity.setIsRelease("f");
		templateWebsiteEntity.setInitUseCnt(0);
		templateWebsiteEntity.setLayout(layout);
		templateWebsiteEntity.setMetadata(SystemConstant.DEFAULT_METADATA);
		templateWebsiteService.update(templateWebsiteEntity);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("templatewebsite:delete")
	public R delete(@RequestBody Integer[] ids){
		templateWebsiteService.deleteBatch(ids);
		
		return R.ok();
	}
	
	/**
	 * 逻辑删除
	 */
	@RequestMapping("/logic_del/{id}")
	//@RequiresPermissions("templatewebsite:logicDel")
	public R logicDel(@PathVariable("id") Integer id) {
		templateWebsiteService.logicDel(id);
		return R.ok();
	}
	
}
