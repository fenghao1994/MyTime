package com.example.mytime.util;

import java.text.SimpleDateFormat;

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


}
