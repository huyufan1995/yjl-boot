package io.renren.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern; 

public class HTMLSpirit{
    /**
     * 去除html代码中含有的标签
     * @param htmlStr
     * @return
     */
    public static String delHtmlTags(String htmlStr) {
        //定义script的正则表达式，去除js可以防止注入
        String scriptRegex="<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
        String styleRegex="<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
        String htmlRegex="<[^>]+>";
        //定义空格,回车,换行符,制表符
        String spaceRegex = "\\s*|\t|\r|\n";

        // 过滤script标签
        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
        htmlStr = htmlStr.replaceAll(spaceRegex, "");
        return htmlStr.trim(); // 返回文本字符串
    }
    /**
     * 获取HTML代码里的内容
     * @param htmlStr
     * @return
     */
    public static String getTextFromHtml(String htmlStr){
        //去除html标签
        htmlStr = delHtmlTags(htmlStr);
        //去除空格" "
        htmlStr = htmlStr.replaceAll(" ","");
        htmlStr = htmlStr.replaceAll("&nbsp;","");
        return htmlStr;
    }

    public static void main(String[] args){
        String htmlStr= "<p style=\"\">213<img alt=\"2.png\" src=\"https://yykj-public-image-1252188577.cos.ap-beijing.myqcloud.com/yjl/simditor/201911/54fd0782e7c8470a9517b3806e8d7034.png\" width=\"294\" height=\"260\"></p>";
        System.out.println(getTextFromHtml(htmlStr));
    }

}