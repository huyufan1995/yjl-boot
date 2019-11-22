package io.renren.api.controller;

import com.google.common.collect.Maps;
import io.renren.api.dto.SessionMember;
import io.renren.api.dto.VerifyApplyDto;
import io.renren.api.dto.VerifyMemberInfoDto;
import io.renren.api.dto.VerifyRecordInfoDto;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.ApplyVerifyEntity;
import io.renren.cms.entity.VerifyRecordEntity;
import io.renren.cms.service.ApplyRecordService;
import io.renren.cms.service.ApplyVerifyService;
import io.renren.cms.service.VerifyRecordService;
import io.renren.utils.Query;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ApiVerifyController
 * @Description TODO
 * @Author moran
 * @Date 2019/11/19 11:40
 **/
@Api("核销")
@Slf4j
@RestController
@RequestMapping("/api/verify")
public class ApiVerifyController {

    @Autowired
    private ApplyRecordService applyRecordService;

    @Autowired
    private VerifyRecordService verifyRecordService;

    @Autowired
    private ApplyVerifyService applyVerifyService;

    @PostMapping("/verifyMemberInfo")
    @ApiOperation(value = "参会人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "参会人员会员code", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
    })
    public ApiResult verifyMemberInfo(@RequestParam String code, @ApiIgnore @TokenMember SessionMember sessionMember) {
        List<VerifyMemberInfoDto> verifyMemberInfoList = applyRecordService.queryVerifyMember(code);
        if(verifyMemberInfoList.isEmpty()){
            return ApiResult.error(500,"该人员没有参与活动");
        }
        HashMap<String,Object> param = new HashMap<>(1);
        List<ApplyVerifyEntity> applyVerifyEntities = applyVerifyService.queryListWithVerifyMember(param);
        if(applyVerifyEntities.isEmpty()){
            return ApiResult.error(500,"该管理人员没有核销权限");

        }
        for (ApplyVerifyEntity verifyEntity : applyVerifyEntities) {
            Integer applyId = verifyEntity.getApplyId();
            for (VerifyMemberInfoDto memberInfoDto : verifyMemberInfoList) {
                if(applyId.toString().equals(memberInfoDto.getId())){
                    return ApiResult.ok(memberInfoDto);
                }
            }
        }
        return ApiResult.error(500,"核销失败，请联系管理员");
    }

    @PostMapping("/affirmMemberInfo")
    @ApiOperation(value = "确认核销")
    @Transactional
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "memberId", value = "memberId", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "applyId", value = "活动Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
    })
    public ApiResult verifyMemberInfo(@RequestParam String memberId, @RequestParam String applyId, @ApiIgnore @TokenMember SessionMember sessionMember) {
        /*  HashMap<String,Object> map = new HashMap<>(3);
        map.put("applyId",applyId);
        map.put("memberId",memberId);
        map.put("openid",sessionMember.getOpenid());
        int total = verifyRecordService.queryTotal(map);
        if(total>0){
            return ApiResult.error(500,"已经核销");
        }*/
        VerifyRecordEntity entity = new VerifyRecordEntity();
        entity.setApplyId(Integer.parseInt(applyId));
        entity.setCtime(new Date());
        entity.setMemberId(Integer.parseInt(memberId));
        entity.setOpenid(sessionMember.getOpenid());
        verifyRecordService.save(entity);
        verifyRecordService.updateVerifyStatus(memberId,applyId);
        return ApiResult.ok();
    }

    @PostMapping("/verifyRecord")
    @ApiOperation(value = "核销记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true)
    })
    public ApiResult verifyRecord(@ApiIgnore @TokenMember SessionMember sessionMember) {
        HashMap<String,Object> param = new HashMap<>(3);
        /*param.put("page",page);
        param.put("limit",limit);*/
        param.put("openid",sessionMember.getOpenid());
        Query query = new Query(param);
        Calendar calendar = Calendar.getInstance();
        List<VerifyApplyDto> verifyApplyDtoList = verifyRecordService.queryVerifyRecord(query);
        for (VerifyApplyDto verifyApplyDto : verifyApplyDtoList) {
            calendar.setTime(verifyApplyDto.getEndTime());					//放入Date类型数据
            verifyApplyDto.setKey(calendar.get(Calendar.YEAR)+"");
            verifyApplyDto.setEndTimeStr(new SimpleDateFormat("MM-dd hh:mm").format(verifyApplyDto.getEndTime()).toString());
            //通过活动ID 查询设置核销记录 人员头像
            List<String> portrait = verifyRecordService.queryPortrait(verifyApplyDto.getId());
            verifyApplyDto.setPortrait(portrait);

        }
        Set<String> keys = verifyApplyDtoList.stream().map(VerifyApplyDto::getKey).collect(Collectors.toSet());
        Set<String> sort = new TreeSet<String>();
        sort.addAll(keys);
        HashMap<String,List<VerifyApplyDto>> dtoMap = Maps.newHashMap();
        sort.forEach(item ->{
            dtoMap.put(item,verifyApplyDtoList.stream().filter(b -> b.getKey().equals(item)).collect(Collectors.toList()));
        });
        return ApiResult.ok(dtoMap);
    }

    @PostMapping("/verifyRecordPeopleInfo")
    @ApiOperation(value = "核销详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "applyId", value = "活动Id", required = true),
    })
    public ApiResult verifyRecordPeopleInfo(@ApiIgnore @TokenMember SessionMember sessionMember,@RequestParam String applyId) {
        HashMap<String,Object> param = new HashMap<>(3);
        param.put("applyId",applyId);
        param.put("openid",sessionMember.getOpenid());
        List<VerifyRecordInfoDto> verifyRecordInfoDtoList = verifyRecordService.queryVerifyPeopleInfo(param);
        Assert.isNullApi(verifyRecordInfoDtoList,"核销详情为空");
        for (VerifyRecordInfoDto infoDto : verifyRecordInfoDtoList) {
            infoDto.setVerifyTimeStr(new SimpleDateFormat("hh:mm").format(infoDto.getCtime()).toString());
        }
        return ApiResult.ok(verifyRecordInfoDtoList);
    }
}
