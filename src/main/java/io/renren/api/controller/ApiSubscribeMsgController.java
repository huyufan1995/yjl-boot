package io.renren.api.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.api.dto.MsgData;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.properties.YykjProperties;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @ClassName ApiSubscribeMsgController
 * @Description TODO
 * @Author moran
 * @Date 2019/11/23 14:22
 **/
@Api("活动订阅消息发送")
@Slf4j
@RestController
@RequestMapping("/api/subscribeMsgSend")
public class ApiSubscribeMsgController {
    @Autowired
    private YykjProperties yykjProperties;

    @PostMapping("/sendMsg")
    @ApiOperation(value = "订阅消息发送)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true)
    })
    public ApiResult memberInfo(@ApiIgnore @TokenMember SessionMember sessionMember) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=AppId&secret=AppSecret";
        url = url.replace("AppId", yykjProperties.getAppid());
        url = url.replace("AppSecret",yykjProperties.getSecret());
        String result = HttpUtil.get(url);
        JSONObject jsonObject = new JSONObject(result);
        Object access_token = jsonObject.get("access_token");
        url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token="+access_token;
        HashMap<String,Object> params = new HashMap<>();
        params.put("touser",sessionMember.getOpenid());
        params.put("template_id","ATbf_EHzqt4pFH0mvy92IZDZm4hB-jqxfnukRRfYl5o");
        params.put("page","pages/bm/info/info");
        HashMap<String,Object> data = new HashMap<>();
        HashMap<String,Object> h1 =new HashMap<>(1);
        h1.put("value","thing2");
        HashMap<String,Object> h4 =new HashMap<>(1);
        h4.put("value","thing4");
        HashMap<String,Object> d2 =new HashMap<>(1);
        String time =new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        d2.put("value",time);
        data.put("thing2",h1);
        data.put("thing4",h4);
        data.put("date3",d2);
        params.put("data",data);
        String s = JSONUtil.toJsonStr(params);
        String post = HttpUtil.post(url, s);
        return ApiResult.ok(post);
    }
}