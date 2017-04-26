package com.example.mytime.util;

import com.example.mytime.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fenghao02 on 2017/4/25.
 */

//多云,少云,晴,阴,小雨,雨,雷阵雨,中雨,阵雨,零散阵雨,零散雷雨,小雪,雨夹雪,阵雪,霾,暴雨,大雨,大雪,中雪
public class WeatherUtil {
    public static final int[] weatherIcon = {R.drawable.w00, R.drawable.w01, R.drawable.w02, R.drawable.w03, R.drawable.w04, R.drawable.w05, R.drawable.w06,
            R.drawable.w07, R.drawable.w08, R.drawable.w09, R.drawable.w10, R.drawable.w11, R.drawable.w12, R.drawable.w13, R.drawable.w14, R.drawable.w15,
            R.drawable.w16, R.drawable.w17, R.drawable.w18, R.drawable.w19, R.drawable.w20, R.drawable.w21, R.drawable.w22, R.drawable.w23, R.drawable.w24,
            R.drawable.w25, R.drawable.w26, R.drawable.w27, R.drawable.w28, R.drawable.w29, R.drawable.w30, R.drawable.w31, R.drawable.w53, R.drawable.w301, R.drawable.w302,
            R.drawable.w02, R.drawable.w03, R.drawable.w04};
    public static final String[] weatherName = {"晴", "多云", "阴", "阵雨", "雷阵雨", "雷阵雨伴有冰雹", "雨夹雪", "小雨",
            "中雨", "大雨", "暴雨", "大暴雨", "特大暴雨", "阵雪", "小雪", "中雪", "大雪", "暴雪", "雾", "冻雨", "沙尘暴",
            "小到中雨", "中到大雨", "大到暴雨", "暴雨到大暴雨", "大暴雨到特大暴雨", "小到中雪", "中到大雪", "大到暴雪",
            "浮尘", "扬沙", "强沙尘暴", "霾", "雨", "雪", "少云", "零散阵雨", "零散雷雨"};
    public static Map<String, Integer> map;

    public static Map getWeahterIcon(){
        map = new HashMap<>();
        for (int i = 0 ; i < weatherIcon.length; i++){
            map.put(weatherName[i], weatherIcon[i]);
        }
        return map;
    }

    public static int getLikeMapPath(String str){
        if (str != null){
            Map<String, Integer> map = getWeahterIcon();
            for (Map.Entry<String, Integer> entry: map.entrySet()){
                if (str.indexOf(entry.getKey()) > -1){
                    return entry.getValue();
                }
            }
        }
        return 0;
    }
}
