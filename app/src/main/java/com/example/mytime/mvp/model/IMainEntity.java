package com.example.mytime.mvp.model;

import com.example.mytime.event.LocalEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public interface IMainEntity {
    //获取天气信息
    WeatherEntity getWeatherInfo(LocalEvent localEvent);
}
