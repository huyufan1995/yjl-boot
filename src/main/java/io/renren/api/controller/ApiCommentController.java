package io.renren.api.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.SessionMember;
import io.renren.api.exception.ApiException;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.CommentEntity;
import io.renren.cms.service.CommentService;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.MemberTypeEnum;
import io.renren.enums.ResponseCodeEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.R;
import io.renren.utils.annotation.TokenMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * 评论
 */
@RestController
@RequestMapping("/api/comment")
@Api("评论")
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private YykjProperties yykjProperties;

    @PostMapping("/addComment")
    @ApiOperation(value = "添加评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "informationId", value = "资讯Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "remark", value = "评论内容", required = true),
    })
    public R addLike(@ApiIgnore CommentEntity commentEntity, @ApiIgnore @TokenMember SessionMember sessionMember) {
        String type = sessionMember.getType();
        if (MemberTypeEnum.COMMON.getCode().toLowerCase().equals(type)) {
            throw new ApiException(SystemConstant.MEMBER_TYPE_MSG, 10001);
        }
        final WxMaService wxMaService = WxMaConfiguration.getMaService(yykjProperties.getAppid());
        if(!wxMaService.getSecCheckService().checkMessage(commentEntity.getRemark())) {
            return R.error(ResponseCodeEnum.SEC_MSG_ILLEGALITY);
        }
        commentEntity.setCtime(new Date());
        commentService.save(commentEntity);
        return R.ok("评论成功");
    }
}
