package io.renren.api.controller;

import io.renren.api.dto.InformationsEntityDto;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResultList;
import io.renren.cms.entity.ApplyEntity;
import io.renren.cms.service.ApplyService;
import io.renren.utils.annotation.IgnoreAuth;
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

import java.util.List;
import java.util.Map;
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

    @IgnoreAuth
    @ApiOperation(value = "活动列表", notes = "活动分页列表",response = ApplyEntity.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false)
    })
    @PostMapping("/list")
    public ApiResultList list(@ApiIgnore() @RequestParam Map<String, Object> params,@ApiIgnore @TokenMember SessionMember sessionMember) {
        params.put("isDel","f");
        List<ApplyEntity> applyEntityList = applyService.queryList(params);
        return  null;
    }
}
