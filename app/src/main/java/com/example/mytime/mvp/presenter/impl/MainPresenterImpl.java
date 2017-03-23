package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.event.LocalEvent;
import com.example.mytime.mvp.model.IMainEntity;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.model.impl.MainEntityImpl;
import com.example.mytime.mvp.presenter.IMainPresenter;
import com.example.mytime.mvp.ui.view.IMainView;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public class MainPresenterImpl implements IMainPresenter {

    IMainEntity iMainEntity;
    IMainView iMainView;

    public MainPresenterImpl(IMainView iMainView) {
        this.iMainView = iMainView;
        iMainEntity = new MainEntityImpl();
    }

    @Override
    public void getWeatherInfo(LocalEvent localEvent) {
        WeatherEntity weatherEntity = iMainEntity.getWeatherInfo(localEvent);
        iMainView.showWeather( weatherEntity);
    }
}
