package io.renren.cms.job;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.service.ApplyService;
import io.renren.cms.service.WxUserService;
import io.renren.config.WxMaConfiguration;
import io.renren.properties.YykjProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName SubscribeMsgJob
 * @Description TODO
 * @Author moran
 * @Date 2019/11/28 9:49
 **/
@Component("subscribeMsgJob")
public class SubscribeMsgJob {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private YykjProperties yykjProperties;

    public void job()throws Exception{
        final WxMaService wxService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
        String accessToken = wxService.getAccessToken();
        List<SendJobEntity> sendJobEntityList= applyService.querySendJob();
        sendJobEntityList.forEach( item->{
            DateTime startTime = new DateTime(new SimpleDateFormat("yyyy-MM-dd").format(item.getStartTime()).toString());
            DateTime now = new DateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
            if(now.plusDays(1).equals(startTime)){
                send(accessToken,item.getOpenid(),item.getApplyTitle(),new SimpleDateFormat("yyyy-MM-dd").format(item.getStartTime()).toString());
            }
        });
    }

    public void send(String accessToken,String openid,String applyTitle,String applyTime){
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+accessToken;
        HashMap<String,Object> params = new HashMap<>();
        params.put("touser",openid);
        params.put("template_id","ATbf_EHzqt4pFH0mvy92IZDZm4hB-jqxfnukRRfYl5o");
        params.put("page","pages/bm/info/info");
        HashMap<String,Object> data = new HashMap<>();
        HashMap<String,Object> h1 =new HashMap<>(1);
        h1.put("value",applyTitle);
        HashMap<String,Object> h4 =new HashMap<>(1);
        h4.put("value","活动马上开始");
        HashMap<String,Object> d2 =new HashMap<>(1);
        d2.put("value",applyTime);
        data.put("thing2",h1);
        data.put("thing4",h4);
        data.put("date3",d2);
        params.put("data",data);
        String s = JSONUtil.toJsonStr(params);
        String post = HttpUtil.post(url, s);
        System.out.println(applyTitle);
        System.out.println(post);
    }

}
