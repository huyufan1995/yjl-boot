package io.renren.api.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import io.renren.cms.service.SubscibeService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板消息处理器
 * @author yujia
 *
 */
@Slf4j
@Component
public class TemplateMsgHandler {

	@Autowired
	private SubscibeService subscibeService;
	@Autowired
	private YykjProperties yykjProperties;

	public boolean send(String page, String toUserOpenid, String templateId, String formId,
			List<WxMaTemplateData> data) {
		log.info("同步发送模板消息开始===");
		try {
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			WxMaTemplateMessage templateMessage = new WxMaTemplateMessage();
			templateMessage.setToUser(toUserOpenid);
			templateMessage.setTemplateId(templateId);
			templateMessage.setPage(page);
			templateMessage.setFormId(formId);
			templateMessage.setData(data);
			wxMaService.getMsgService().sendTemplateMsg(templateMessage);
		} catch (Exception e) {
			log.error("同步发送模板消息异常{}", e.getMessage());
			return false;
		}
		subscibeService.deleteByFormid(formId);
		log.info("同步发送模板消息结束===");
		return true;
	}

	@Async
	public Future<Boolean> sendAsync(String page, String toUserOpenid, String templateId, String formId,
			List<WxMaTemplateData> data) {
		log.info("异步发送模板消息开始===");
		try {
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			WxMaTemplateMessage templateMessage = new WxMaTemplateMessage();
			templateMessage.setToUser(toUserOpenid);
			templateMessage.setTemplateId(templateId);
			templateMessage.setPage(page);
			templateMessage.setFormId(formId);
			templateMessage.setData(data);
			wxMaService.getMsgService().sendTemplateMsg(templateMessage);
		} catch (Exception e) {
			log.error("异步发送模板消息异常{}", e.getMessage());
			return new AsyncResult<Boolean>(false);
		}
		subscibeService.deleteByFormid(formId);
		log.info("异步发送模板消息结束===");
		return new AsyncResult<Boolean>(true);
	}

	@Async
	public Future<Boolean> sendAsync(String page, String toUserOpenid, String templateId, String formId,
			String... args) {
		log.info("异步发送模板消息开始===");
		try {
			List<WxMaTemplateData> templateDataList = new ArrayList<>(args.length);
			for (int i = 0; i < args.length; i++) {
				templateDataList.add(new WxMaTemplateData("keyword" + (i + 1), args[i]));
			}
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			WxMaTemplateMessage templateMessage = new WxMaTemplateMessage();
			templateMessage.setToUser(toUserOpenid);
			templateMessage.setTemplateId(templateId);
			templateMessage.setPage(page);
			templateMessage.setFormId(formId);
			templateMessage.setData(templateDataList);
			wxMaService.getMsgService().sendTemplateMsg(templateMessage);
		} catch (Exception e) {
			log.error("异步发送模板消息异常{}", e.getMessage());
			return new AsyncResult<Boolean>(false);
		}
		subscibeService.deleteByFormid(formId);
		log.info("异步发送模板消息结束===");
		return new AsyncResult<Boolean>(true);
	}

	@Async
	public Future<Boolean> sendAsyncDelay(String page, String toUserOpenid, String templateId, String formId,
			List<WxMaTemplateData> data, long delay) {
		log.info("异步延时发送模板消息开始,延时{}===", delay);
		try {
			Thread.sleep(delay);
			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			WxMaTemplateMessage templateMessage = new WxMaTemplateMessage();
			templateMessage.setToUser(toUserOpenid);
			templateMessage.setTemplateId(templateId);
			templateMessage.setPage(page);
			templateMessage.setFormId(formId);
			templateMessage.setData(data);
			wxMaService.getMsgService().sendTemplateMsg(templateMessage);
		} catch (Exception e) {
			log.error("异步延时发送模板消息异常{}", e.getMessage());
			return new AsyncResult<Boolean>(false);
		}
		subscibeService.deleteByFormid(formId);
		log.info("异步延时发送模板消息结束,延时{}===", delay);
		return new AsyncResult<Boolean>(true);
	}

	@Async
	public Future<Boolean> sendAsyncDelay(String page, String toUserOpenid, String templateId, String formId,
			long delay, String... args) {
		log.info("异步延时发送模板消息开始,延时{}===", delay);
		try {
			Thread.sleep(delay);
			List<WxMaTemplateData> templateDataList = new ArrayList<>(args.length);
			for (int i = 0; i < args.length; i++) {
				templateDataList.add(new WxMaTemplateData("keyword" + (i + 1), args[i]));
			}

			final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
			WxMaTemplateMessage templateMessage = new WxMaTemplateMessage();
			templateMessage.setToUser(toUserOpenid);
			templateMessage.setTemplateId(templateId);
			templateMessage.setPage(page);
			templateMessage.setFormId(formId);
			templateMessage.setData(templateDataList);
			wxMaService.getMsgService().sendTemplateMsg(templateMessage);
		} catch (Exception e) {
			log.error("异步延时发送模板消息异常{}", e.getMessage());
			return new AsyncResult<Boolean>(false);
		}
		subscibeService.deleteByFormid(formId);
		log.info("异步延时发送模板消息结束,延时{}===", delay);
		return new AsyncResult<Boolean>(true);
	}

}
