package io.renren.api.vo;

import io.renren.api.vo.box.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TemplateVo extends TemplateWebsiteEntityVo{
    private String index;
    private String phone;
    private String share;
    private String font_color;
    private Box1 box1;
    private Box2 box2;
    private Box3 box3;
    private Box4 box4;
    private Box5 box5;
    private Box6 box6;
    private Box7 box7;
    private Box8 box8;
}