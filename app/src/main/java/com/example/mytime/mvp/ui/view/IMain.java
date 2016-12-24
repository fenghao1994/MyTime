package com.example.mytime.mvp.ui.view;

/**
 * Created by fenghao on 2016/12/24.
 * mianActivity接口
 */

public interface IMain {

    //展示天气
    void showWeather();

    //点击天气栏
    void clickWeather();

    //展示计划fragment
    void showPlanFragment();

    //展示便签fragment
    void showNoteFragment();

    //点击添加fab
    void clickFab();

}
