package com.example.mytime.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytime.R;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by fenghao02 on 2017/3/27.
 */

public class WeatherInfoFragment extends Fragment {

    private WeatherEntity mWeatherEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void showWeather(WeatherEvent event){
        if ( event.isSuccess()){
            mWeatherEntity = event.getWeatherEntity();
            Log.i("WeatherInfoFragment", "clf ---- 》 " + mWeatherEntity.toString());
        }else {

        }

    }
}
