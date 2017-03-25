package com.example.mytime.util;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fenghao on 2017/1/4.
 */

public class MyUtil {

    public static String dateYMDHM(long time){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd HH:mm");
        String s = sdf.format( time);
        return s;
    }

    public static String dateYMD(long time){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd");
        String s = sdf.format( time);
        return s;
    }

    public static String dateHM(long time){
        SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm");
        String s = sdf.format( time);
        return s;
    }

    /**
     * 是否是闰年
     * @return
     */
    public static boolean isRun(int year){
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
            return true;
        }
        return false;
    }

    /**
     * 正则表达式 判断 手机号
     * @param mobiles
     * @return
     */
    public static final boolean isMobileNumber(String mobiles){
        Pattern pattern = Pattern.compile("^((1[358][0-9])|(14[57])|(17[0678]))\\d{8}$");
        Matcher matcher = pattern.matcher( mobiles);
        return matcher.matches();
    }


}
