package io.renren.cms.service.impl;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.google.common.base.Splitter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.renren.api.constant.SystemConstant;
import io.renren.cms.dao.GifTemplateDao;
import io.renren.cms.dao.PraiseRecordGifDao;
import io.renren.cms.dao.UseRecordDao;
import io.renren.cms.dao.WxUserDao;
import io.renren.cms.entity.GifTemplateEntity;
import io.renren.cms.entity.PraiseRecordGifEntity;
import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.entity.WxUserEntity;
import io.renren.cms.service.GifTemplateService;
import io.renren.utils.validator.Assert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("gifTemplateService")
public class GifTemplateServiceImpl implements GifTemplateService {
	@Autowired
	private GifTemplateDao gifTemplateDao;
	@Autowired
	private WxUserDao wxUserDao;
	@Autowired
	private UseRecordDao useRecordDao;
	@Autowired
	private PraiseRecordGifDao praiseRecordGifDao;
	
	@Value("${yykj.temppath}")
	private String temppath;
	@Value("${yykj.giftemplatepath}")
	private String giftemplatepath;
	@Value("${yykj.uploaddir}")
	private String uploaddir;

	final String COMMAND_300 = "ffmpeg -i %s -r 6 -vf ass=%s,scale=300:-1 -y %s";
	final String COMMAND_250 = "ffmpeg -i %s -r 6 -vf ass=%s,scale=250:-1 -y %s";
	final String COMMAND_180 = "ffmpeg -i %s -r 5 -vf ass=%s,scale=180:-1 -y %s";

	@Override
	public GifTemplateEntity queryObject(Integer id) {
		return gifTemplateDao.queryObject(id);
	}

	@Override
	public List<GifTemplateEntity> queryList(Map<String, Object> map) {
		return gifTemplateDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return gifTemplateDao.queryTotal(map);
	}

	@Override
	public void save(GifTemplateEntity gifTemplate) {
		gifTemplateDao.save(gifTemplate);
	}

	@Override
	public void update(GifTemplateEntity gifTemplate) {
		gifTemplateDao.update(gifTemplate);
	}

	@Override
	public void delete(Integer id) {
		gifTemplateDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		gifTemplateDao.deleteBatch(ids);
	}

	@Override
	@Transactional
	public UseRecordEntity generate(Integer id, String sentences, String openid) throws Exception {
		GifTemplateEntity gifTemplateEntity = queryObject(id);
		Assert.isNullApi(gifTemplateEntity, "该模板不存在");
		String assPath = renderAss(id, sentences);
		String fileName = UUID.randomUUID() + ".gif";
		String gifPath = Paths.get(uploaddir).resolve("tmp/" + fileName).toString();
		String videoPath = Paths.get(giftemplatepath).resolve(gifTemplateEntity.getVideoPath()).toString();
		String cmd = String.format(COMMAND_300, videoPath, assPath, gifPath);
		log.info("cmd: {}", cmd);
		long start = System.currentTimeMillis();
		try {
			Process exec = Runtime.getRuntime().exec(cmd);
			exec.waitFor();
		} catch (Exception e) {
			log.error("生成gif报错：{}", e);
		}

		long end = System.currentTimeMillis();

		// 使用记录
		UseRecordEntity useRecordEntity = new UseRecordEntity();
		WxUserEntity wxUserEntity = wxUserDao.queryByOpenId(openid);
		Assert.isNullApi(wxUserEntity, "该用户不存在");
		if (StringUtils.isNotBlank(wxUserEntity.getAvatarUrl())) {
			useRecordEntity.setAvatarUrl(wxUserEntity.getAvatarUrl());
		}
		useRecordEntity.setConsumeTime(end - start);
		useRecordEntity.setOpenid(wxUserEntity.getOpenId());
		useRecordEntity.setTemplateId(gifTemplateEntity.getId());
		useRecordEntity.setTemplateImageExample(gifTemplateEntity.getCover());
		useRecordEntity.setTemplateName(gifTemplateEntity.getName());
		useRecordEntity.setUseTime(new Date());
		useRecordEntity.setTemplateImageResult("tmp/" + fileName);
		useRecordEntity.setType("dynamic");// 动态图片
		useRecordEntity.setWidth(gifTemplateEntity.getWidth());
		useRecordEntity.setHeight(gifTemplateEntity.getHeight());
		useRecordDao.save(useRecordEntity);

		// 使用量
		gifTemplateEntity.setUseCnt(gifTemplateEntity.getUseCnt() + 1);
		update(gifTemplateEntity);
		
		//是否点赞
		PraiseRecordGifEntity praiseRecordGifEntity = praiseRecordGifDao.queryObjectByTemplateIdAndOpenId(openid, id);
		useRecordEntity.setIfPraise(praiseRecordGifEntity == null ? SystemConstant.F_STR : SystemConstant.T_STR);
		return useRecordEntity;

	}

	private String renderAss(Integer id, String sentences) throws Exception {
		Path path = Paths.get(temppath).resolve(UUID.randomUUID().toString().replace("-", "") + ".ass");
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_26);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setDirectoryForTemplateLoading(Paths.get(giftemplatepath).resolve(id + "").toFile());
		Map<String, Object> root = new HashMap<>();
		Map<String, String> mx = new HashMap<>();
		List<String> list = Splitter.on(",").splitToList(sentences);
		for (int i = 0; i < list.size(); i++) {
			mx.put("sentences" + i, list.get(i));
		}
		root.put("mx", mx);
		Template temp = cfg.getTemplate("template.ftl");
		try (FileWriter writer = new FileWriter(path.toFile())) {
			temp.process(root, writer);
		} catch (Exception e) {
			log.error("生成ass文件报错", e);
		}
		return path.toString();
	}

}
