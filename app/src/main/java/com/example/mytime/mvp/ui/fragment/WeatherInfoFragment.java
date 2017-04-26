package com.example.mytime.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.ui.activity.WeatherActivity;
import com.example.mytime.mvp.ui.adapter.FutureWeatherAdapter;
import com.example.mytime.util.WeatherUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fenghao02 on 2017/3/27.
 */

public class WeatherInfoFragment extends Fragment {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.city_name)
    TextView mCityName;
    @BindView(R.id.week)
    TextView mWeek;
    @BindView(R.id.temperature)
    TextView mTemperature;
    @BindView(R.id.weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.weather_dec)
    TextView mWeatherDec;
    @BindView(R.id.wind)
    TextView mWind;
    @BindView(R.id.humidity)
    TextView mHumidity;
    @BindView(R.id.dressing_index)
    TextView mDressingIndex;
    @BindView(R.id.cold_index)
    TextView mColdIndex;
    @BindView(R.id.sun_rise)
    TextView mSunRise;
    @BindView(R.id.sun_set)
    TextView mSunSet;
    @BindView(R.id.air_condition)
    TextView mAirCondition;
    @BindView(R.id.exercise_index)
    TextView mExerciseIndex;
    Unbinder unbinder;
    @BindView(R.id.show_list)
    ImageView mShowList;
    @BindView(R.id.recycler_weather)
    RecyclerView mRecyclerWeather;
    private WeatherEntity mWeatherEntity;

    private Map<String, Integer> map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        map = WeatherUtil.getWeahterIcon();
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
    public void showWeather(WeatherEvent event) {
        if (event.isSuccess()) {
            mWeatherEntity = event.getWeatherEntity();
            WeatherEntity.ResultBean resultBean = mWeatherEntity.getResult().get(0);

            List<WeatherEntity.ResultBean.FutureBean> futureBeanList = resultBean.getFuture();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerWeather.setLayoutManager( linearLayoutManager);
            FutureWeatherAdapter adapter = new FutureWeatherAdapter(getActivity(), futureBeanList);
            mRecyclerWeather.setAdapter( adapter);

            mCityName.setText(resultBean.getCity());
            mWeek.setText(resultBean.getWeek());
            mTemperature.setText(resultBean.getTemperature());
            mWeatherDec.setText(resultBean.getWeather());
            int drawablePath = WeatherUtil.getLikeMapPath(resultBean.getWeather());
            mWeatherIcon.setImageDrawable(getActivity().getResources().getDrawable(drawablePath));
            mWind.setText(resultBean.getWind());
            mHumidity.setText(resultBean.getHumidity());
            mDressingIndex.setText(resultBean.getDressingIndex());
            mColdIndex.setText(resultBean.getColdIndex());
            mSunRise.setText(resultBean.getSunrise());
            mSunSet.setText(resultBean.getSunset());
            mAirCondition.setText(resultBean.getAirCondition());
            mExerciseIndex.setText(resultBean.getExerciseIndex());
        } else {

        }
    }

    @OnClick(R.id.show_list)
    public void show_list() {
        ((WeatherActivity) getActivity()).showNavLayout();
    }

    @OnClick(R.id.back)
    public void back() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
