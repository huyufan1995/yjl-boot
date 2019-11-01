package io.renren.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 * 
 * @author yujia
 * @email yujiain2008@gmail.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    
    
    public static Long getDistanceDays(Date startDate, Date endDate) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
//        long hour = 0;
//        long min = 0;
//        long sec = 0;
        long time1 = startDate.getTime();
        long time2 = endDate.getTime();
        long diff;
        diff = time2 - time1;
        day = diff / (24 * 60 * 60 * 1000);
//        hour = (diff / (60 * 60 * 1000) - day * 24);
//        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
//        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day;
    }
    
    /**
     * 获取指定日期后的日期
     * @param date
     * @param day
     * @return
     */
    public static Date getDateAfter(Date date, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(date);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);  
        return now.getTime();  
    }
    
    /**
     * 获取指定日期前的日期
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return now.getTime();  
    }   
    
    
    /**
     * 获取指定日期前的日期
     * @param d
     * @param day
     * @return
     */
    public static String getStrDateBefore(Date d, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return format(now.getTime(), DATE_PATTERN);  
    }   
}
