package io.renren.api.controller;

import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.LeaveEntityDto;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.LeaveEntity;
import io.renren.cms.entity.MemberEntity;
import io.renren.cms.service.CollectService;
import io.renren.cms.service.LeaveService;
import io.renren.cms.service.LikeService;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.MemberType;
import io.renren.utils.annotation.TokenMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @ClassName ApiLeaveController
 * @Description 留言
 * @Author moran
 * @Date 2019/11/18 16:17
 **/
@RestController
@RequestMapping("/api/leave")
@Api("留言")
public class ApiLeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private LikeService likeService;



    @PostMapping("/leaveList")
    @ApiOperation(value = "留言列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "memberId", value = "被查看人会员ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
    })
    public ApiResult leaveList(@ApiIgnore @RequestParam Map<String, Object> params,@ApiIgnore @TokenMember SessionMember sessionMember) {
        Query query = new Query(params);
        List<LeaveEntityDto> leaveEntityList = leaveService.queryListByMemberId(query);
        HashMap<String,Object> map = new HashMap<>(3);
        for (LeaveEntityDto leaveEntityDto : leaveEntityList) {
            map.put("dataId",leaveEntityDto.getId());
            map.put("openid",sessionMember.getOpenid());
            map.put("likeType",SystemConstant.MEMBER_MSG);
            //此留言是否点赞
            leaveEntityDto.setIsLike(likeService.queryTotal(map)>0);
        }
        return ApiResult.ok(leaveEntityList);
    }

    @PostMapping("/addLeave")
    @MemberType
    @ApiOperation(value = "会员添加留言")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "memberId", value = "被查看人会员ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "content", value = "留言内容", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true),
    })
    public ApiResult addLeave(@RequestParam String memberId,@RequestParam String content,@ApiIgnore @TokenMember SessionMember sessionMember) {
        LeaveEntity leaveEntity = new LeaveEntity();
        leaveEntity.setMemberId(Integer.parseInt(memberId));
        leaveEntity.setOpenid(sessionMember.getOpenid());
        leaveEntity.setContent(content);
        leaveEntity.setStatus("f");
        leaveEntity.setIsDel("f");
        leaveEntity.setCtime(new Date());
        leaveService.save(leaveEntity);
        return ApiResult.ok();
    }
}
