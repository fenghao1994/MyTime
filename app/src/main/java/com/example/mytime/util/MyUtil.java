package com.example.mytime.util;

import android.content.Context;
import android.content.res.Resources;

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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        Resources resources=context.getResources();
        if(resources!=null){
            final float scale =resources.getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);

        }
        return (int) (dpValue *2.0 + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
