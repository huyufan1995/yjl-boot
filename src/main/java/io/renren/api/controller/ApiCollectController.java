package io.renren.api.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.CollectEntity;
import io.renren.cms.entity.CommentEntity;
import io.renren.cms.service.CollectService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.MemberTypeEnum;
import io.renren.enums.ResponseCodeEnum;
import io.renren.utils.R;
import io.renren.utils.annotation.TokenMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.HashMap;

/**
 * 收藏
 *
 * @author moran
 * @date 2019-11-11
 */
@RestController
@RequestMapping("/api/collect")
@Api("收藏")
public class ApiCollectController {

    @Autowired
    private CollectService collectService;

    @PostMapping("/addComment")
    @ApiOperation(value = "添加收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "dataId", value = "数据ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "collectType", value = "收藏类型 1：资讯 3：活动 4：会员", required = true),
    })
    public ApiResult addLike(@ApiIgnore CollectEntity collectEntity, @ApiIgnore @TokenMember SessionMember sessionMember) {
        String type = sessionMember.getType();
        if (MemberTypeEnum.COMMON.getCode().toLowerCase().equals(type)) {
            throw new ApiException(SystemConstant.MEMBER_TYPE_MSG, 10001);
        }
        HashMap<String,Object> params = new HashMap<>(5);
        params.put("dataId",collectEntity.getDataId());
        params.put("collectType",collectEntity.getCollectType());
        params.put("openid",sessionMember.getOpenid());
        if(collectService.queryTotal(params) == 0){
            collectEntity.setOpenid(sessionMember.getOpenid());
            collectEntity.setCtime(new Date());
            collectService.save(collectEntity);
            return ApiResult.ok("收藏成功");
        }else {
           Boolean delFlag = collectService.deleteWithOpenIdAndCollectTypeAndDataId(params);
        }
        return ApiResult.ok("取消收藏");
    }
}
