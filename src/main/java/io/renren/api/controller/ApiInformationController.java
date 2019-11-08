package io.renren.api.controller;

import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.InformationsEntityDto;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ApiResultList;
import io.renren.cms.entity.BannerEntity;
import io.renren.cms.entity.InformationTypeEntity;
import io.renren.cms.entity.InformationsEntity;
import io.renren.cms.service.BannerService;
import io.renren.cms.service.CommentService;
import io.renren.cms.service.InformationService;
import io.renren.cms.service.InformationTypeService;
import io.renren.utils.HTMLSpirit;
import io.renren.utils.Query;
import io.renren.utils.RedisUtils;
import io.renren.utils.annotation.ExtApiIdempotent;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资讯
 */
@RestController
@RequestMapping("/api/information")
@Api("资讯")
public class ApiInformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private InformationTypeService informationTypeService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private CommentService commentService;

    @IgnoreAuth
    @ApiOperation(value = "资讯列表", notes = "资讯分页列表",response = InformationsEntityDto.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "keyword", value = "关键词", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "informationType", value = "资讯类型", required = false)
    })
    @PostMapping("/list")
    public ApiResultList list(@ApiIgnore() @RequestParam Map<String, Object> params) {
        params.put("isDel", SystemConstant.F_STR);
        Query query = new Query(params);
        List<InformationsEntityDto> informationList = informationService.queryListDto(query);
        //对内容过滤html标签，并确认是否是50字以内
        for (InformationsEntityDto entityDto : informationList) {
            String content = HTMLSpirit.getTextFromHtml(entityDto.getContent());
            if(content.length()>50){
                content=content.substring(0,50);
            }
            entityDto.setContent(content);
            List<String> portrait = commentService.queryPortrait(entityDto.getId());
            entityDto.setPortrait(portrait);
        }
        BannerEntity bannerEntity = bannerService.queryObject(1);
        Map<Object,Object> map = new HashMap<>();
        map.put("banner",bannerEntity);
        map.put("list",informationList);
        return new ApiResultList(map);
    }

    @IgnoreAuth
    @ApiOperation(value = "资讯分类列表", notes = "资讯分类列表")
    @PostMapping("/information_type_list")
    public ApiResult list() {
        List<InformationTypeEntity> informationTypeEntities = informationTypeService.queryList(null);
        return ApiResult.ok(informationTypeEntities);
    }

    @IgnoreAuth
    @ApiOperation("资讯数据详情")
    @PostMapping("/information_data_info")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "数据ID", required = true)
    })
    public ApiResult applyDataInfo(@RequestParam Integer id) {
        InformationsEntity informationEntity = informationService.queryObject(id);
        Assert.isNullApi(informationEntity, "该数据不存在");
        return ApiResult.ok(informationEntity.getContent());
    }

/*    @IgnoreAuth
    @ApiOperation("测试接口幂等性")
    @PostMapping("/informationTest")
    @ExtApiIdempotent(type = "head")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "type", value = "数据ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "token", value = "token", required = true)
    })
    public ApiResult test(@RequestParam String type,@RequestParam String token){
        InformationsEntity entity = new InformationsEntity();
        entity.setIsDel("t");
        entity.setType(Integer.parseInt(type));
        informationService.save(entity);
        return ApiResult.ok();
    }
    @IgnoreAuth
    @ApiOperation("生成token")
    @PostMapping("/informationToken")
    @ApiImplicitParams({
    })
    public ApiResult token(HttpServletRequest request){
        String token =  UUID.randomUUID().toString();
        redisUtils.set(token,token,30000);
        request.setAttribute("token",token);
        return ApiResult.ok();
    }*/
}
