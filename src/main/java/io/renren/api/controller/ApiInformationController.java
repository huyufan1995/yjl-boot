package io.renren.api.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.CommentEntityDto;
import io.renren.api.dto.InformationsEntityDto;
import io.renren.api.dto.InformationsEntityInfoDto;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ApiResultList;
import io.renren.cms.dao.InformationBrowsDao;
import io.renren.cms.entity.*;
import io.renren.cms.service.*;
import io.renren.properties.YykjProperties;
import io.renren.utils.HTMLSpirit;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

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
    private InformationBrowsService informationBrowsService;

    @Autowired
    private InformationTypeService informationTypeService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private YykjProperties yykjProperties;

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
        Object informationType = params.get("informationType");
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
            List<String> portrait = informationBrowsService.queryPortrait(entityDto.getId().toString());
            entityDto.setPortrait(portrait);
        }
        Map<Object,Object> map = new HashMap<>();
        //只有热门类型才会加载首页banner
        if(Integer.parseInt(informationType.toString())!=1){
            map.put("banner",null);
        }else{
            BannerEntity bannerEntity = bannerService.queryObject(1);
            map.put("banner",bannerEntity);
        }
        map.put("list",informationList);
        return new ApiResultList(map);
    }

    @IgnoreAuth
    @ApiOperation(value = "资讯分类列表", notes = "资讯分类列表")
    @PostMapping("/information_type_list")
    public ApiResult informationTypeList() {
        List<InformationTypeEntity> informationTypeEntities = informationTypeService.queryList(null);
        return ApiResult.ok(informationTypeEntities);
    }

    @IgnoreAuth
    @ApiOperation(value = "资讯评论列表", notes = "资讯评论列表")
    @PostMapping("/information_comment_list")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "informationId", value = "资讯数据ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true)
    })
    public ApiResult commentList(@ApiIgnore() @RequestParam Integer informationId,@RequestParam String openid) {
        HashMap<String,Object> params = new HashMap<>(1);
        params.put("informationId",informationId);
        List<CommentEntityDto> commentEntities = commentService.queryListDto(params);
        for (CommentEntityDto commentEntity : commentEntities) {
            if(commentEntity.getOpenid().equals(openid)){
                commentEntity.setLikeFlag(true);
            }else{
                commentEntity.setLikeFlag(false);
            }
        }
        return ApiResult.ok(commentEntities);
    }

    @IgnoreAuth
    @ApiOperation("资讯数据详情")
    @PostMapping("/information_data_info")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "id", value = "资讯数据ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true)
    })
    public ApiResult applyDataInfo(@RequestParam Integer id,@RequestParam String openid) {
        InformationsEntity informationEntity = informationService.queryObject(id);
        Assert.isNullApi(informationEntity, "该数据不存在");
        HashMap<String,Object> params = new HashMap<>();
        params.put("openid",openid);
        params.put("informationId",id);
        //资讯浏览量
        int total = informationBrowsService.queryTotal(params);
        InformationsEntityInfoDto informationsEntityInfoDto = new InformationsEntityInfoDto();
        BeanUtil.copyProperties(informationEntity, informationsEntityInfoDto, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        informationsEntityInfoDto.setBrowsTotal(total==0?total+1:total);
        //查询当前资讯是否收藏
        Boolean collectFlag = collectService.isCollect(id,1,openid);
        informationsEntityInfoDto.setCollectFlag(collectFlag);
        //添加资讯浏览记录
        addInformationBrows(id, openid);
        return ApiResult.ok(informationsEntityInfoDto);
    }

    /**
     * 添加资讯浏览记录
     * @param id
     * @param openid
     */
    private void addInformationBrows(Integer id, String openid) {
        InformationBrowsEntity informationBrowsEntity = new InformationBrowsEntity();
        informationBrowsEntity.setCtime(new Date());
        informationBrowsEntity.setInformationId(id.toString());
        informationBrowsEntity.setOpenid(openid);
        informationBrowsService.save(informationBrowsEntity);
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
