package com.example.mytime.mvp.ui.view;

import com.example.mytime.event.LocalEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao on 2016/12/24.
 * mianActivity接口
 */

public interface IMainView {

    //点击天气栏
    void clickWeather();

    //展示计划fragment
    void showPlanFragment();

    //展示便签fragment
    void showNoteFragment();

    //点击添加fab
    void clickFab();

    //获取天气预报

    void showWeather(WeatherEntity entity);

}
