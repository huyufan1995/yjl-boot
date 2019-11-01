package io.renren.cms.job;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import io.renren.api.kit.WeixinAppKit;
import io.renren.api.vo.wx.Keyword;
import io.renren.api.vo.wx.WxResult;
import io.renren.cms.component.AccessTokenComponent;
import io.renren.cms.entity.SubscibeEntity;
import io.renren.cms.service.SubscibeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Job {

	@Autowired
	private SubscibeService subscibeService;
	@Autowired
	private AccessTokenComponent accessTokenComponent;
	
	private int totalDay = 1;

//	@Scheduled(cron = "0 30 11 * * ?")
	public void sendTemplate() {
		log.info("发送模板消息任务开始。。。");
		if(totalDay < 3) {
			log.info("不满足发送天数：{}", totalDay);
			totalDay++;
			return;
		}
		int ok = 0;
		int error = 0;
		totalDay = 1;
		HashMap<Object, Object> data = Maps.newHashMap();
		data.put("keyword1", new Keyword("偷瞄一下，最新进展！朋友圈达人都在玩的趣图和海报....."));
		DateTime yesterday = DateUtil.yesterday();
		data.put("keyword2", new Keyword(DateUtil.formatDate(yesterday)));
		// 已经订阅用户
		List<SubscibeEntity> subscibeList = subscibeService.queryListByStatus();
		if (CollectionUtil.isNotEmpty(subscibeList)) {
			for (SubscibeEntity s : subscibeList) {
				WxResult wxResult = WeixinAppKit.sendTemplate(accessTokenComponent.getAccessToken(), s.getFormid(),
						WeixinAppKit.APP_PAGE_INDEX, WeixinAppKit.APP_TEMPLATE_ID_WALLPAPER, s.getOpenid(), data);
				if (wxResult.getErrcode() == 0) {
					subscibeService.update(s);
					ok++;
				} else {
					error++;
				}
			}
		}
		log.info("发送模板消息结束,共计{}，成功{}，失败{}", subscibeList.size(), ok, error);
	}

}