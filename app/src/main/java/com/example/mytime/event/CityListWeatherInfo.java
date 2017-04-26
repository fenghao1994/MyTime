package com.example.mytime.event;

import com.example.mytime.mvp.model.entity.WeatherEntity;

/**
 * Created by fenghao02 on 2017/4/26.
 */

public class CityListWeatherInfo {
    WeatherEntity.ResultBean resultBean;

    public WeatherEntity.ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(WeatherEntity.ResultBean resultBean) {
        this.resultBean = resultBean;
    }
}
