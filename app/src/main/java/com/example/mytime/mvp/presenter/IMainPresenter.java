package com.example.mytime.mvp.presenter;

import com.example.mytime.event.LocalEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public interface IMainPresenter {
    //展示天气
    void getWeatherInfo(LocalEvent localEvent);
}
