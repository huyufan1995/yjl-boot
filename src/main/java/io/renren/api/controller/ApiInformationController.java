package io.renren.api.controller;

import io.renren.api.constant.SystemConstant;
import io.renren.api.vo.ApiResult;
import io.renren.cms.entity.InformationsEntity;
import io.renren.cms.service.InformationService;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
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

import java.util.List;
import java.util.Map;

/**
 * 资讯
 */
@RestController
@RequestMapping("/api/information")
@Api("资讯")
public class ApiInformationController {

    @Autowired
    private InformationService informationService;
    @IgnoreAuth
    @ApiOperation(value = "资讯列表", notes = "资讯分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false) })
    @PostMapping("/list")
    public ApiResult list(@ApiIgnore() @RequestParam Map<String, Object> params) {
        params.put("isDel", SystemConstant.F_STR);
        Query query = new Query(params);
        List<InformationsEntity> informationList = informationService.queryList(query);
        return ApiResult.ok(informationList);
    }

    @ApiOperation("资讯数据详情")
    @PostMapping("/information_data_info")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "数据ID", required = true)
    })
    public ApiResult applyDataInfo(@RequestParam Integer id) {
        InformationsEntity informationEntity = informationService.queryObject(id);
        Assert.isNullApi(informationEntity, "该数据不存在");
        return ApiResult.ok(informationEntity.getDesc());
    }
}
