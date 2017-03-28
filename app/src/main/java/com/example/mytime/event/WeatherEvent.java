package com.example.mytime.event;

import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao02 on 2017/3/28.
 */

public class WeatherEvent {
    private WeatherEntity mWeatherEntity;
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public WeatherEvent() {
    }

    public WeatherEvent(WeatherEntity weatherEntity, boolean isSuccess) {
        mWeatherEntity = weatherEntity;
        this.isSuccess = isSuccess;
    }
    public WeatherEntity getWeatherEntity() {
        return mWeatherEntity;
    }

    public void setWeatherEntity(WeatherEntity weatherEntity) {
        mWeatherEntity = weatherEntity;
    }
}
