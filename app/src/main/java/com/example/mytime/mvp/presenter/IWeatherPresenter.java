package com.example.mytime.mvp.presenter;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public interface IWeatherPresenter {

    /**
     * 获取城市列表
     */
    void getCitysList();

    /**
     * 展示天气信息
     */
    void getWeatherInfo(String city);
}
