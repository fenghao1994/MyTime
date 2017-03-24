package com.example.mytime.mvp.ui.view;

import com.example.mytime.mvp.model.entity.Citys;
import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public interface IWeatherView {

    /**
     * 展示城市列表
     * @param citys
     */
    void showCitysList(Citys citys);

    /**
     * 展示天气信息
     * @param weatherEntity
     */
    void showWeatherInfo(WeatherEntity weatherEntity);
}
