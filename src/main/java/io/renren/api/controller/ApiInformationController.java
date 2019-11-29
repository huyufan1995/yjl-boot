package io.renren.api.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Page;
import com.qcloud.cos.COSClient;
import io.renren.api.constant.SystemConstant;
import io.renren.api.dto.CommentEntityDto;
import io.renren.api.dto.InformationsEntityDto;
import io.renren.api.dto.InformationsEntityInfoDto;
import io.renren.api.dto.SessionMember;
import io.renren.api.vo.ApiResult;
import io.renren.api.vo.ApiResultList;
import io.renren.cms.entity.*;
import io.renren.cms.service.*;
import io.renren.config.WxMaConfiguration;
import io.renren.enums.AuditStatusEnum;
import io.renren.properties.YykjProperties;
import io.renren.utils.HTMLSpirit;
import io.renren.utils.ProjectUtils;
import io.renren.utils.Query;
import io.renren.utils.annotation.IgnoreAuth;
import io.renren.utils.annotation.TokenMember;
import io.renren.utils.validator.Assert;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.util.*;

/**
 * 资讯
 */
@RestController
@RequestMapping("/api/information")
@Api("资讯")
@Slf4j
public class ApiInformationController {

    @Autowired
    private InformationService informationService;
    @Autowired
    private COSClient cosClient;

    @Autowired
    private InformationBrowsService informationBrowsService;

    @Autowired
    private InformationTypeService informationTypeService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private YykjProperties yykjProperties;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LikeService likeService;

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
        params.put("showStatus",SystemConstant.T_STR);
        params.put("auditStatus", AuditStatusEnum.PASS);
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
        if(!informationType.equals("1")){
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
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码 默认1", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "limit", value = "页大小 默认5", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "informationId", value = "资讯数据ID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "openid", value = "openid", required = true)
    })
    public ApiResult commentList(@ApiIgnore() @RequestParam Map<String, Object> params,@RequestParam String openid) {
        params.put("openid","");
        Query query = new Query(params);
        List<CommentEntityDto> commentEntities = commentService.queryListDto(query);
        for (CommentEntityDto commentEntity : commentEntities) {
            HashMap<String,Object> map = new HashMap<>(3);
            map.put("dataId",commentEntity.getId());
            map.put("likeType","2");
            commentEntity.setLikeTotal(likeService.queryTotal(map));
            //查询当前人是否点赞
            map.put("openid",openid);
            commentEntity.setIsLike(likeService.queryTotal(map)>0);
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
        HashMap<String,Object> qid = new HashMap<>(1);
        qid.put("informationId",id);
        int total = informationBrowsService.queryTotal(qid);
        InformationsEntityInfoDto informationsEntityInfoDto = new InformationsEntityInfoDto();
        BeanUtil.copyProperties(informationEntity, informationsEntityInfoDto, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        informationsEntityInfoDto.setBrowsTotal(informationEntity.getBrowsTotal()+total);
        //查询当前资讯是否收藏
        Boolean collectFlag = collectService.isCollect(id,1,openid);
        informationsEntityInfoDto.setIsCollect(collectFlag);
        //查询是否点赞资讯    likeType 1 为资讯相关
        params.put("likeType","1");
        params.put("dataId",id);
        int likeTotal = likeService.queryTotal(params);
        if(likeTotal>0){
            informationsEntityInfoDto.setIsLike(true);
        }
        //添加资讯浏览记录
        addInformationBrows(id, openid);
        //添加资讯评论数量
        addInformationComment(informationsEntityInfoDto);
/*        //设置资讯点赞数量  将openId的过滤条件取消，查询所有点赞相关数量
        params.put("openid","");
        params.put("likeType","2");
        informationsEntityInfoDto.setLikeTotal(likeService.queryTotal(params));*/

        //设置资讯详情描述
        String content = HTMLSpirit.getTextFromHtml(informationsEntityInfoDto.getContent());
        if(content.length()>100){
            content=content.substring(0,100);
        }
        informationsEntityInfoDto.setInformationDesc(content);
        //处理字符间距
        String s =informationsEntityInfoDto.getContent().replaceFirst("style=\"","style=\"letter-spacing:1px;");
        informationsEntityInfoDto.setContent(s);
        return ApiResult.ok(informationsEntityInfoDto);
    }
    //添加资讯评论数量
    private void addInformationComment(InformationsEntityInfoDto informationsEntityInfoDto) {
        HashMap<String,Object> map = new HashMap<>(1);
        map.put("informationId",informationsEntityInfoDto.getId());
        int commentTotal = commentService.queryTotal(map);
        informationsEntityInfoDto.setCommentTotal(commentTotal);
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

    @ApiOperation("资讯分享")
    @PostMapping("/information_share_image")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "informationId", value = "资讯Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "token", value = "令牌", required = true)
    })
    public ApiResult viewShareImage(@RequestParam Integer informationId,@ApiIgnore @TokenMember SessionMember sessionMember) {
        MemberEntity memberEntity = memberService.queryObject(sessionMember.getMemberId());
        InformationsEntity informationsEntity = informationService.queryObject(informationId);
        Assert.isNullApi(informationsEntity, "资讯不存在");
        TemplateEntity templateEntity = templateService.queryObject(503);
        Assert.isNullApi(templateEntity, "模板不存在");
        templateEntity.setImageTemplate(yykjProperties.getVisitprefix() + templateEntity.getImageTemplate());
        List<TemplateItmeEntity> templateItmeList = templateService.queryListByTemplateId(503);
        if (CollectionUtil.isEmpty(templateItmeList)) {
            return ApiResult.error(500, "模板参数不存在");
        }
        for (TemplateItmeEntity templateItme : templateItmeList) {
            //小程序码
            if (templateItme.getId().equals(2652)) {
              templateItme.setImagePath(informationsEntity.getQrCode());
            }
            //banner
            if (templateItme.getId().equals(2650)) {
                templateItme.setImagePath(SystemConstant.DEFAULT_TEXT_IMG);
            }
            //会员头像
            if (templateItme.getId().equals(2651)) {
                templateItme.setImagePath(memberEntity.getPortrait());
            }
            //标题
            if (templateItme.getId().equals(2653)) {
                templateItme.setDescribe(informationsEntity.getTitle().replaceAll(SystemConstant.REGEX_CASE_INTRO, "$1∫"));
            }
            //详情
            if (templateItme.getId().equals(2654)) {
                //设置资讯详情描述
                String content = HTMLSpirit.getTextFromHtml(informationsEntity.getContent());
                if(content.length()>50){
                    content=content.substring(0,50)+"...";
                }
                templateItme.setDescribe(content.replaceAll(SystemConstant.REGEX_CASE_INTRO, "$1∫"));
            }
            //会员名
            if (templateItme.getId().equals(2655)) {
                templateItme.setDescribe(memberEntity.getNickname());

            }
        }
        File generateShareImage = ProjectUtils.generateShareImage(templateItmeList, templateEntity);
        String shareImageUrl = ProjectUtils.uploadCosFile(cosClient, generateShareImage);
        String fullUrl = yykjProperties.getImagePrefixUrl() + shareImageUrl;
        log.info("==============资讯分享图片：{}", fullUrl);
        return ApiResult.ok(fullUrl);
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
