package com.example.mytime.mvp.model.impl;

import com.example.mytime.event.LocalEvent;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.IMainEntity;
import com.example.mytime.mvp.model.entity.WeatherEntity;
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

public class MainEntityImpl implements IMainEntity {

    private WeatherEntity mWeatherEntity;
    private WeatherEvent mWeatherEvent;

    @Override
    public WeatherEntity getWeatherInfo(LocalEvent localEvent) {
        Weather api = (Weather) MobAPI.getAPI(Weather.NAME);
        String city = localEvent.getCity();
        if (city.contains("市")){
            //// TODO: 2017/3/28  匹配规则有问题  沙市 津市 呼市郊区
            city = city.substring(0, city.length() - 1);
        }
        api.queryByCityName(city, new APICallback() {
            @Override
            public void onSuccess(API api, int i, Map<String, Object> map) {
                Gson gson = new Gson();
                String result = gson.toJson( map);
                mWeatherEntity = gson.fromJson(result, WeatherEntity.class);
                mWeatherEvent = new WeatherEvent(mWeatherEntity, true);
                EventBus.getDefault().post( mWeatherEvent);
            }

            @Override
            public void onError(API api, int i, Throwable throwable) {
                mWeatherEvent = new WeatherEvent(null, false);
                EventBus.getDefault().post( mWeatherEvent);
            }
        });
        return mWeatherEntity;
    }
}
