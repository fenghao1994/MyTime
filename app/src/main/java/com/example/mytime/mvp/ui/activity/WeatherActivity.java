package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Citys;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.presenter.IWeatherPresenter;
import com.example.mytime.mvp.presenter.impl.WeatherPresenterImpl;
import com.example.mytime.mvp.ui.view.IWeatherView;

public class WeatherActivity extends AppCompatActivity implements IWeatherView {

    IWeatherPresenter weatherPresenter;
    WeatherEntity weatherEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weath);
        weatherPresenter = new WeatherPresenterImpl( this);
        Intent intent = getIntent();
        weatherEntity = (WeatherEntity) intent.getSerializableExtra("WEATHER");
        showWeatherInfo( weatherEntity);

    }
    public void getCitys(View view){
        weatherPresenter.getCitysList();
    }

    @Override
    public void showCitysList(Citys citys) {
        String a = "";
    }

    @Override
    public void showWeatherInfo(WeatherEntity weatherEntity) {
        String a = "";
    }
}
