package io.renren.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.renren.api.dto.*;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ApiResultList;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.entity.ApplyRecordEntity;
import io.renren.cms.entity.InformationsEntity;
import io.renren.cms.entity.LikeEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.cms.service.ApplyService;
import io.renren.utils.HTMLSpirit;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.MemberType;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动
 *
 * @author moran
 * @date 2019-11-11
 */
@RestController
@RequestMapping("/api/apply")
@Api("活动")
public class ApiApplyController {


    @Autowired
    private ApplyService applyService;

    @Autowired
    private ApplyRecordService applyRecordService;

    @IgnoreAuth
    @ApiOperation(value = "活动列表", notes = "活动分页列表",response = ApplyEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = false),
    })
    @PostMapping("/list")
    public ApiResult list(@ApiIgnore() @RequestParam Map<String, Object> params, @ApiIgnore @TokenMember SessionMember sessionMember) {
        params.put("isDel","f");
        params.put("auditStatus","pass");
        params.put("showStatus","t");
        List<ApplyEntityDto> applyEntityListDto = applyService.queryListDto(params);
        //对内容过滤html标签，并确认是否是50字以内
        for (ApplyEntityDto entityDto : applyEntityListDto) {
            String content = HTMLSpirit.getTextFromHtml(entityDto.getApplyContent());
            if(content.length()>50){
                content=content.substring(0,50);
            }
            entityDto.setApplyContent(content);
            HashMap<String,Object> query = new HashMap<>(3);
            query.put("applyId",entityDto.getId());
            query.put("offset",0);
            query.put("limit",4);
            List<ApplyRecordEntiyDto> applyRecordEntiyDtoList = applyRecordService.queryPortrait(query);
            // 处理浏览人头像
            entityDto.setPortrait(applyRecordEntiyDtoList.stream().map(ApplyRecordEntiyDto::getPortrait).collect(Collectors.toList()));
            if(new Date().after(entityDto.getStartTime())){
                entityDto.setApplyStatus("已结束");
            }else{
                if(applyRecordEntiyDtoList.isEmpty()){
                    entityDto.setApplyStatus("立即报名");

                }else{
                    for (ApplyRecordEntiyDto recordEntiyDto : applyRecordEntiyDtoList) {
                        if(sessionMember.getOpenid().equals(recordEntiyDto.getOpenid())){
                            if(entityDto.getId().equals(recordEntiyDto.getApplyId())){
                                entityDto.setApplyStatus("已报名");
                            }else{
                                entityDto.setApplyStatus("立即报名");
                            }
                        }
                    }
                }
            }
        }
        return  ApiResult.ok(applyEntityListDto);
    }

    @PostMapping("/addApply")
    @ApiOperation(value = "报名")
    @MemberType
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "applyId", value = "活动Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "likeType", value = "点赞类型 1：资讯 2：评论 3：活动", required = true)
    })
    public ApiResult addLike(@RequestParam("dataId") String applyId,@ApiIgnore@TokenMember SessionMember sessionMember) {
        HashMap<String,Object> params = new HashMap<>(5);
        params.put("applyId",applyId);
        params.put("openid",sessionMember.getOpenid());
        if(applyRecordService.queryTotal(params) == 0){
            ApplyRecordEntity applyRecordEntity = new ApplyRecordEntity();
            applyRecordEntity.setApplyId(applyId);
            applyRecordEntity.setOpenid(sessionMember.getOpenid());
            applyRecordEntity.setCtime(new Date());
            applyRecordService.save(applyRecordEntity);
        }else{
            Boolean isApply = applyRecordService.deleteByOpenIdAndApplyId(params);
            if(!isApply){
                throw new ApiException("取消报名失败", 100111);
            }
        }
        return  ApiResult.ok();
    }

    @IgnoreAuth
    @ApiOperation("活动数据详情")
    @PostMapping("/apply_data_info")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "活动ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true)
    })
    public ApiResult applyDataInfo(@RequestParam Integer id,@RequestParam String openid) {
        ApplyEntityDto applyEntityDto = applyService.findAllById(id);
        Assert.isNullApi(applyEntityDto, "该数据不存在");
            if(new Date().after(applyEntityDto.getStartTime())){
                applyEntityDto.setApplyStatus("已结束");
            }else{
                for (ApplyRecordEntiyDto recordEntiyDto : applyEntityDto.getApplyRecordEntiyDto()) {
                    if(openid.equals(recordEntiyDto.getOpenid())){
                        if(applyEntityDto.getId().equals(recordEntiyDto.getApplyId())){
                            applyEntityDto.setApplyStatus("已报名");
                        }else{
                            applyEntityDto.setApplyStatus("立即报名");
                        }
                    }
                }
            }
        return ApiResult.ok(applyEntityDto);
    }
}
