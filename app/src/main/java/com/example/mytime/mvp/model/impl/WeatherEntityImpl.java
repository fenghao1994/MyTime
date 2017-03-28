package com.example.mytime.mvp.model.impl;

import android.util.Log;

import com.example.mytime.mvp.model.IWeatherEntity;
import com.example.mytime.mvp.model.entity.Citys;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.ui.view.IWeatherView;
import com.google.gson.Gson;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.Weather;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public class WeatherEntityImpl implements IWeatherEntity {
    private Citys mCitys;
    private WeatherEntity mWeatherEntity;

    @Override
    public Citys getCitysList() {
        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
        api.getSupportedCities(new APICallback() {
            @Override
            public void onSuccess(API api, int i, Map<String, Object> map) {
                Gson gson = new Gson();
                String str = gson.toJson( map);
                mCitys = gson.fromJson(str, Citys.class);
                EventBus.getDefault().post( mCitys);
            }

            @Override
            public void onError(API api, int i, Throwable throwable) {
                String a = "";
            }
        });

        return mCitys;
    }

    @Override
    public WeatherEntity getWeatherInfo(String city) {
        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
        api.queryByCityName(city, new APICallback() {
            @Override
            public void onSuccess(API api, int i, Map<String, Object> map) {
                Gson gson = new Gson();
                String result = gson.toJson( map);
                mWeatherEntity = gson.fromJson(result, WeatherEntity.class);
            }

            @Override
            public void onError(API api, int i, Throwable throwable) {
            }
        });
        return mWeatherEntity;
    }
}
