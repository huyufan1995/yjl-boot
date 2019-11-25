package io.renren.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.*;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.*;
import io.renren.cms.service.*;
import io.renren.properties.YykjProperties;
import io.renren.utils.HTMLSpirit;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.MemberType;
import io.renren.utils.annotation.TokenMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 活动
 *
 * @author moran
 * @date 2019-11-11
 */
@RestController
@Slf4j
@RequestMapping("/api/apply")
@Api("活动")
public class ApiApplyController {


    @Autowired
    private ApplyService applyService;

    @Autowired
    private ApplyBannerService applyBannerService;

    @Autowired
    private YykjProperties yykjProperties;

    @Autowired
    private ApplyReviewService applyReviewService;

    @Autowired
    private ApplyRecordService applyRecordService;

    @Autowired
    private CollectService collectService;

    @IgnoreAuth
    @ApiOperation(value = "活动列表", notes = "活动分页列表", response = ApplyEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "openid", value = "openid", required = false)
    })
    @PostMapping("/list")
    public ApiResult list(@ApiIgnore() @RequestParam Map<String, Object> params) {
        params.put("isDel", "f");
        params.put("auditStatus", "pass");
        params.put("showStatus", "t");
        Query query = new Query(params);
        List<ApplyEntityDto> applyEntityListDto = applyService.queryListDto(query);
        //对内容过滤html标签，并确认是否是50字以内
        for (ApplyEntityDto entityDto : applyEntityListDto) {
            String content = HTMLSpirit.getTextFromHtml(entityDto.getApplyContent());
            if (content.length() > 50) {
                content = content.substring(0, 50);
            }
            entityDto.setApplyContent(content);
            HashMap<String, Object> q = new HashMap<>(3);
            q.put("applyId", entityDto.getId());
            List<ApplyRecordEntiyDto> applyRecordEntiyDtoList = applyRecordService.queryPortrait(q);
            // 处理报名人头像
            entityDto.setPortrait(applyRecordEntiyDtoList.stream().map(ApplyRecordEntiyDto::getPortrait).limit(5).collect(Collectors.toList()));
            if (new Date().after(entityDto.getStartTime())) {
                entityDto.setApplyStatus("已结束");
            } else {
                if (applyRecordEntiyDtoList.isEmpty()) {
                    entityDto.setApplyStatus("立即报名");
                } else {
                    //根据当前所以报名人的头像集合遍历是否有 报名
                    Loop:for (ApplyRecordEntiyDto recordEntiyDto : applyRecordEntiyDtoList) {
                        if (params.get("openid").toString().equals(recordEntiyDto.getOpenid())) {
                            entityDto.setApplyStatus("已报名");
                            break Loop;
                        }
                    }
                    if(StringUtils.isEmpty(entityDto.getApplyStatus())){
                        entityDto.setApplyStatus("立即报名");
                    }
                }
            }
        }
        List<ApplyBannerEntity> applyBannerEntities = applyBannerService.queryList(null);
        Map<String,Object> result = new HashMap<>(2);
        result.put("list",applyEntityListDto);
        result.put("banner",applyBannerEntities);
        return ApiResult.ok(result);
    }

    @PostMapping("/addApply")
    @ApiOperation(value = "报名")
    @MemberType
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "活动Id", required = true)
    })
    public ApiResult addLike(@RequestParam("applyId") String applyId,@ApiIgnore @TokenMember SessionMember sessionMember) {
        HashMap<String, Object> params = new HashMap<>(5);
        params.put("applyId", applyId);
        params.put("openid", sessionMember.getOpenid());
        if (applyRecordService.queryTotal(params) == 0) {
            ApplyRecordEntity applyRecordEntity = new ApplyRecordEntity();
            applyRecordEntity.setApplyId(applyId);
            applyRecordEntity.setOpenid(sessionMember.getOpenid());
            applyRecordEntity.setCtime(new Date());
            applyRecordEntity.setMemberId(sessionMember.getMemberId());
            applyRecordEntity.setVerifyStatus(SystemConstant.F_STR);
            applyRecordService.save(applyRecordEntity);
            sendAsync(sessionMember,applyId);
        } else {
            Boolean isApply = applyRecordService.deleteByOpenIdAndApplyId(params);
            if (!isApply) {
                throw new ApiException("取消报名失败", 100111);
            }
        }
        return ApiResult.ok();
    }
    @Async
    public Future<Boolean> sendAsync(SessionMember sessionMember, String applyId) {
        log.info("异步发送模板消息开始===");
        try {
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
            ApplyEntity applyEntity = applyService.queryObject(Integer.parseInt(applyId));
            HashMap<String,Object> data = new HashMap<>();
            HashMap<String,Object> h1 =new HashMap<>(1);
            h1.put("value",applyEntity.getApplyTitle());
            HashMap<String,Object> h4 =new HashMap<>(1);
            h4.put("value","活动加入成功");
            HashMap<String,Object> d2 =new HashMap<>(1);
            String time =new SimpleDateFormat("yyyy-MM-dd").format(applyEntity.getStartTime()).toString();
            d2.put("value",time);
            data.put("thing2",h1);
            data.put("thing4",h4);
            data.put("date3",d2);
            params.put("data",data);
            String s = JSONUtil.toJsonStr(params);
            String post = HttpUtil.post(url, s);
            System.out.println(post);
        } catch (Exception e) {
            log.error("异步发送模板消息异常{}", e.getMessage());
            return new AsyncResult<Boolean>(false);
        }
        log.info("异步发送模板消息结束===");
        return new AsyncResult<Boolean>(true);
    }


    @IgnoreAuth
    @ApiOperation("活动数据详情")
    @PostMapping("/apply_data_info")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "活动ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true)
    })
    public ApiResult applyDataInfo(@RequestParam Integer id, @RequestParam String openid) {
        ApplyEntityDto applyEntityDto = getApplyEntityDto(id, openid);
        return ApiResult.ok(applyEntityDto);
    }

    /**
     * 封装活动详情数据
     * @param id
     * @param openid
     * @return
     */
    private ApplyEntityDto getApplyEntityDto(Integer id,String openid) {
        ApplyEntity applyEntity = applyService.queryObject(id);
        HashMap<String,Object> qs = new HashMap<>(1);
        qs.put("applyId",id);
        List<ApplyRecordEntiyDto> applyRecordEntiyDtoList = applyRecordService.queryPortrait(qs);
        qs.put("showStatus","t");
        qs.put("auditStatus","pass");
        ApplyReviewEntityDto applyReviewEntityDto = applyReviewService.queryObjectDto(qs);
        ApplyEntityDto applyEntityDto = new ApplyEntityDto();
        BeanUtil.copyProperties(applyEntity, applyEntityDto, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        applyEntityDto.setApplyRecordEntiyDto(applyRecordEntiyDtoList);
        applyEntityDto.setApplyReviewEntityDto(applyReviewEntityDto);
        if (new Date().after(applyEntityDto.getStartTime())) {
            applyEntityDto.setApplyStatus("已结束");
        } else {
            if(applyRecordEntiyDtoList.isEmpty()){
                applyEntityDto.setApplyStatus("立即报名");
            }
            Loop:
            for (int i = 0; i < applyRecordEntiyDtoList.size(); i++) {
                ApplyRecordEntiyDto recordEntiyDto = applyRecordEntiyDtoList.get(i);
                if (openid.equals(recordEntiyDto.getOpenid())) {
                     applyEntityDto.setApplyStatus("已报名");
                }
            }
            if(StringUtils.isEmpty(applyEntityDto.getApplyStatus())){
                applyEntityDto.setApplyStatus("立即报名");

            }
        }
        HashMap<String, Object> q = new HashMap<>(2);
        q.put("collectType", 3);
        q.put("openid", openid);
        q.put("dataId", applyEntityDto.getId());
        applyEntityDto.setIsCollect(collectService.queryTotal(q) > 0);
        //设置报名人数
        applyEntityDto.setJoinTotal(applyEntityDto.getApplyRecordEntiyDto().size());
        return applyEntityDto;
    }
}
