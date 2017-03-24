package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.IWeatherEntity;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.model.impl.WeatherEntityImpl;
import com.example.mytime.mvp.presenter.IWeatherPresenter;
import com.example.mytime.mvp.ui.view.IWeatherView;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public class WeatherPresenterImpl implements IWeatherPresenter{

    IWeatherView weatherView;
    IWeatherEntity weatherEntity;

    public WeatherPresenterImpl(IWeatherView weatherView) {
        this.weatherView = weatherView;
        weatherEntity = new WeatherEntityImpl();
    }

    @Override
    public void getCitysList() {
        weatherView.showCitysList(weatherEntity.getCitysList());
    }

    @Override
    public void getWeatherInfo(String city) {
        weatherView.showWeatherInfo(weatherEntity.getWeatherInfo( city));
    }
}
