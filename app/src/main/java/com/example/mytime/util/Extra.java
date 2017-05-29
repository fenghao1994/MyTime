package com.example.mytime.util;

import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao on 2017/1/7.
 * 全局常量
 */

public class Extra {
    public static final String ALARM_CLOCK = "COM.EXAMPLE.MYTIME.ALARM_CLOCK";

    public static final String ALARM_LOCATION = "COM.EXAMPLE.MYTIME.ALARM_LOCATION";

    public static final String SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";

    public static final String WIDGET_TIME = "COM.EXAMPLE.WIDGET.TIME_APP_PROVIDER";

    public static final int NET_WORK = 2;//是否请求网络  1 不请求， 2请求

    public static WeatherEntity entity;

    public static WeatherEntity getEntity() {
        return entity;
    }

    public static void setEntity(WeatherEntity entity) {
        Extra.entity = entity;
    }
}
