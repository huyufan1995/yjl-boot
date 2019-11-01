package io.renren.utils;

import java.util.ResourceBundle;

/**
 * 获取 文件上传配置信息
 */
public class TemplateWebsiteConfigUtil {
	
	
	private static final String CONFIG_FILE_NAME = "templatewebsite";   //config为配置文件的名字
	private static ResourceBundle resource = null;
	
    static
    {
    	resource = ResourceBundle.getBundle(CONFIG_FILE_NAME);
    }
    
    public static String getValue(String key)
    {
        String substring = key.substring(0,key.indexOf("."));
        return resource.getString(substring);
    }
 
    
    public static void main(String[] args) {
    	System.out.println(TemplateWebsiteConfigUtil.getValue("1"));
	}
}
