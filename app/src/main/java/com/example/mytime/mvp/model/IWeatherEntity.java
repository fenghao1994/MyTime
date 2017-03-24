package com.example.mytime.mvp.model;

import com.example.mytime.mvp.model.entity.Citys;
import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public interface IWeatherEntity {

    Citys getCitysList();

    WeatherEntity getWeatherInfo(String city);
}
