package io.renren.cms.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.exception.ApiException;
import io.renren.api.kit.PicKit;
import io.renren.cms.dao.TemplateDao;
import io.renren.cms.dao.TemplateItmeDao;
import io.renren.cms.dao.UseRecordDao;
import io.renren.cms.entity.TemplateEntity;
import io.renren.cms.entity.TemplateItmeEntity;
import io.renren.cms.entity.UseRecordEntity;
import io.renren.cms.service.TemplateItmeService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("templateItmeService")
public class TemplateItmeServiceImpl implements TemplateItmeService {
	
	private Logger log = LoggerFactory.getLogger(TemplateItmeServiceImpl.class);
	
	@Autowired
	private TemplateItmeDao templateItmeDao;
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private UseRecordDao useRecordDao;
	@Autowired
	private YykjProperties yykjProperties;


	@Override
	public TemplateItmeEntity queryObject(Integer id) {
		return templateItmeDao.queryObject(id);
	}

	@Override
	public List<TemplateItmeEntity> queryList(Map<String, Object> map) {
		return templateItmeDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return templateItmeDao.queryTotal(map);
	}

	@Override
	public void save(TemplateItmeEntity templateItme) {
		templateItmeDao.save(templateItme);
	}

	@Override
	public void update(TemplateItmeEntity templateItme) {
		templateItmeDao.update(templateItme);
	}

	@Override
	public void delete(Integer id) {
		templateItmeDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		templateItmeDao.deleteBatch(ids);
	}

	@Override
	public List<TemplateItmeEntity> queryListByTemplateId(Integer templateId) {
		return templateItmeDao.queryListByTemplateId(templateId);
	}

	/**
	 * 使用模板生成图片
	 */
	/*@Override
	@Transactional
	public UseRecordEntity use(TemplateItmesForm templateItmesForm) {
		long start = System.currentTimeMillis();
		List<TemplateItmeEntity> items = templateItmesForm.getItems();
		if(CollectionUtil.isEmpty(items)){
			throw new ApiException("参数项不能为空", 500);
		}
		
		Integer templateId = items.get(0).getTemplateId();
		TemplateEntity templateEntity = templateDao.queryObject(templateId);
		
		if(null == templateEntity) {
			throw new ApiException("该模板不存在", 500);
		}
		
		String imageTemplate = templateEntity.getImageTemplate();
		BufferedImage templateBufferedImage = PicKit.loadImageLocal(yykjProperties.getUploaddir().concat(imageTemplate));
		
		//先处理图片
		for (TemplateItmeEntity templateItmeEntity : items) {
			if(SystemConstant.IMAGE_STR.equals(templateItmeEntity.getType())){
				log.info("************图片处理****************" + templateItmeEntity.toString());
				//处理图片
				if(StringUtils.isBlank(templateItmeEntity.getImagePath())){
					continue;
				}
				//检测敏感图片
				String imageUrl = templateItmeEntity.getImagePath();
				String replace = imageUrl.replace(yykjProperties.getVisitprefix(), "");
				String imagePath =  yykjProperties.getUploaddir() + replace;
				
				final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
				try {
					if(!wxMaService.getSecCheckService().checkImage(FileUtil.file(imagePath))) {
						throw new ApiException("图片包含敏感信息", 500);
					}
				} catch (WxErrorException e) {}
				
				if(SystemConstant.IMAGE_SHAPE_JX.equals(templateItmeEntity.getImageShape())){
					//矩形
					BufferedImage bufferedImageIn = PicKit.loadImageUrl(templateItmeEntity.getImagePath());
					Image scale = ImgUtil.scale(bufferedImageIn, templateItmeEntity.getWidth(), templateItmeEntity.getHeight());
					PicKit.writeImg(ImgUtil.toBufferedImage(scale), templateBufferedImage, templateItmeEntity.getX(), templateItmeEntity.getY());
				}else if(SystemConstant.IMAGE_SHAPE_YX.equals(templateItmeEntity.getImageShape())){
					//圆形
					BufferedImage bufferedImageYX = PicKit.convertCircular(PicKit.loadImageUrl(templateItmeEntity.getImagePath()));
					PicKit.writeImg(bufferedImageYX, templateBufferedImage, templateItmeEntity.getX(), templateItmeEntity.getY());
				}
			}
		}
		
		//后处理文字
		for (TemplateItmeEntity templateItmeEntity : items) {
			if(SystemConstant.FONT_STR.equals(templateItmeEntity.getType())){
				log.info("***********文字处理******************" + templateItmeEntity.toString());
				//处理文字
				if(StringUtils.isBlank(templateItmeEntity.getDescribe())){
					continue;
				}
				
				final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
				if (!wxMaService.getSecCheckService().checkMessage(templateItmeEntity.getDescribe())) {
					throw new ApiException(templateItmeEntity.getDescribe() + "包含敏感词汇", 500);
				}
				
				Font font = new Font(templateItmeEntity.getFontName(), templateItmeEntity.getFontStyle(), templateItmeEntity.getFontSize());
				Color fontColor = new Color(templateItmeEntity.getFontColorR(), templateItmeEntity.getFontColorG(), templateItmeEntity.getFontColorB());
				templateItmeEntity.setY(templateItmeEntity.getY() + templateItmeEntity.getFontSize());//TODO
				
				if(templateItmeEntity.getIsMultiLine().equals(SystemConstant.TRUE_STR)){
					//多行
					PicKit.writeStrings(templateBufferedImage, templateItmeEntity.getDescribe().split("∫"), font, fontColor,
							templateItmeEntity.getX(), templateItmeEntity.getY(),
							templateItmeEntity.getWordSpace(), templateItmeEntity.getLineSpace(), false);
				}else{
					//单行
					if(templateItmeEntity.getIsCenter().equals(SystemConstant.TRUE_STR)){
						//水平居中
						PicKit.writeStringCenter(templateBufferedImage, templateItmeEntity.getDescribe(), font, fontColor, templateItmeEntity.getY(), templateItmeEntity.getWordSpace());
					}else{
						//非水平居中
						if(templateItmeEntity.getFontDeletedLine().equals(SystemConstant.TRUE_STR)){
							//删除线
							PicKit.drawDeletedLine(templateBufferedImage, font, fontColor, templateItmeEntity.getDescribe(), templateItmeEntity.getX(), templateItmeEntity.getY(), 0, templateItmeEntity.getDescribe().length());
						}
						
						if(templateItmeEntity.getFontUnderLine().equals(SystemConstant.TRUE_STR)){
							//下化线
							PicKit.drawUnderLine(templateBufferedImage, font, fontColor, templateItmeEntity.getDescribe(), templateItmeEntity.getX(), templateItmeEntity.getY(), 0, templateItmeEntity.getDescribe().length());
						}
						
						if(templateItmeEntity.getFontUnderLine().equals(SystemConstant.FALSE_STR) && templateItmeEntity.getFontDeletedLine().equals(SystemConstant.FALSE_STR)){
							//正常文字
							PicKit.writeString(templateBufferedImage, templateItmeEntity.getDescribe(), font, fontColor, templateItmeEntity.getX(), templateItmeEntity.getY(), templateItmeEntity.getWordSpace());
						}
					}
				}
			}
		}
		
		DateFormat df = new SimpleDateFormat("yyyyMM");
		String datePath = df.format(new Date());
		// 文件保存的相对位置
		String relativePath = "result/" + datePath + "/" + templateItmesForm.getOpenid() + "/";
		String newFileName = start + ".jpg";
		// 文件保存的完整目录
		String absolutePath = yykjProperties.getUploaddir() + relativePath;
		File temp = new File(absolutePath);
		if (!temp.exists()) {
			temp.mkdirs(); // 如果目录不存在则创建
		}
		PicKit.writeImageLocal(absolutePath + newFileName, templateBufferedImage);
		long end = System.currentTimeMillis();
		
		//使用记录
		UseRecordEntity useRecordEntity = new UseRecordEntity();
		
		CardEntity cardEntity = cardDao.queryObjectByOpenid(templateItmesForm.getOpenid());
		if(null != cardEntity && StringUtils.isNotBlank(cardEntity.getPortrait())){
			useRecordEntity.setAvatarUrl(cardEntity.getPortrait());
		} else {
			useRecordEntity.setAvatarUrl(StrUtil.format(SystemConstant.DEFAULT_CARD_RANDOM_PORTRAIT, RandomUtil.randomInt(1, 57)));
		}
		useRecordEntity.setConsumeTime(end - start);
		useRecordEntity.setOpenid(templateItmesForm.getOpenid());
		useRecordEntity.setTemplateId(templateId);
		useRecordEntity.setTemplateImageExample(templateEntity.getImageExample());
		useRecordEntity.setTemplateName(templateEntity.getName());
		useRecordEntity.setUseTime(new Date());
		useRecordEntity.setTemplateImageResult(relativePath + newFileName);
		useRecordEntity.setType("static");//静态图片
		useRecordEntity.setWidth(templateEntity.getWidth());
		useRecordEntity.setHeight(templateEntity.getHeight());
		useRecordDao.save(useRecordEntity);
		
		//使用量
		templateEntity.setUseCnt(templateEntity.getUseCnt() + 1);
		templateDao.update(templateEntity);
		
		//是否点赞
		Map<String, Object> params = new HashMap<>();
		params.put("openId", templateItmesForm.getOpenid());
		params.put("templateId", templateEntity.getId());
		boolean flag = praiseRecordService.ifPraise(params);
		if(flag){
			useRecordEntity.setIfPraise("t");
		}else{
			useRecordEntity.setIfPraise("f");
		}
		return useRecordEntity;
	}*/
}
