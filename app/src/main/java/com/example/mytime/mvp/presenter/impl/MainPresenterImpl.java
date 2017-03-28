package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.event.LocalEvent;
import com.example.mytime.mvp.model.IMainEntity;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.model.impl.MainEntityImpl;
import com.example.mytime.mvp.presenter.IMainPresenter;
import com.example.mytime.mvp.ui.view.IMainView;
import com.google.gson.Gson;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.Weather;

import java.util.Map;

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
        /*WeatherEntity weatherEntity = */
        iMainEntity.getWeatherInfo(localEvent);
//        getWeatherFromNet( localEvent);
//        iMainView.showWeather( weatherEntity);
    }


/*
    //// TODO: 2017/3/28  后面采取接口回调来解决http请求返回值的问题 即weatherEntityImpl 中getWeatherInfo方法需要返回一个weather对象，但是请求网络是异步的，所以可能返回的是null，但由于接口回调的方法的返回值是null，所以不能返回
    public void getWeatherFromNet(LocalEvent localEvent) {
        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
        String city = localEvent.getCity();
        city = city.substring(0, city.length() - 1);
        api.queryByCityName(city, new APICallback() {
            @Override
            public void onSuccess(API api, int i, Map<String, Object> map) {
                Gson gson = new Gson();
                String result = gson.toJson( map);
                WeatherEntity weatherEntity = gson.fromJson(result, WeatherEntity.class);
                iMainView.showWeather( weatherEntity);
            }

            @Override
            public void onError(API api, int i, Throwable throwable) {
                iMainView.showWeather( null);
            }
        });
    }*/
}
