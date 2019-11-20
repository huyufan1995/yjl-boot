package io.renren.api.controller;

import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.LikeEntity;
import io.renren.cms.service.LikeService;
import io.renren.enums.MemberTypeEnum;
import io.renren.utils.R;
import io.renren.utils.annotation.MemberType;
import io.renren.utils.annotation.TokenMember;
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

/**
 * 点赞
 */
@RestController
@RequestMapping("/api/like")
@Api("点赞")
public class ApiLikeController {

    @Autowired
    private LikeService likeService;


    @PostMapping("/addLike")
    @ApiOperation(value = "点赞")
    @MemberType
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "dataId", value = "数据Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "likeType", value = "点赞类型 1：资讯 2：评论 3：活动 4:会员留言 5：点赞会员" , required = true)
    })
    public ApiResult addLike(@RequestParam("dataId") Integer dataId, @RequestParam("likeType") Integer likeType,@ApiIgnore@TokenMember SessionMember sessionMember) {
        HashMap<String,Object> params = new HashMap<>(5);
        params.put("dataId",dataId);
        params.put("likeType",likeType);
        params.put("openId",sessionMember.getOpenid());
        if(likeService.queryTotal(params) == 0){
            LikeEntity likeEntity = new LikeEntity();
            likeEntity.setCtime(new Date());
            likeEntity.setDataId(dataId);
            likeEntity.setOpenid(sessionMember.getOpenid());
            likeEntity.setLikeType(likeType);
            likeService.save(likeEntity);
        }else{
            Boolean aBoolean = likeService.deleteByOpenIdAndLikeTypeAndDataId(params);
        }
        return  ApiResult.ok();
    }
}
