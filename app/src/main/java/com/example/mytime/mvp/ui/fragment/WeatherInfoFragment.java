package com.example.mytime.mvp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.ui.activity.WeatherActivity;
import com.example.mytime.mvp.ui.adapter.FutureWeatherAdapter;
import com.example.mytime.util.ACache;
import com.example.mytime.util.MyUtil;
import com.example.mytime.util.WeatherUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by fenghao02 on 2017/3/27.
 */

public class WeatherInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    private WeatherEntity mWeatherEntity;
    private WeatherEvent mWeatherEvent;
//    private ACache mACache;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_info, container, false);
        unbinder = ButterKnife.bind(this, view);
//        mACache = ACache.get(getActivity());
//        mWeatherEvent = (WeatherEvent) mACache.getAsObject("WEATHEREVENT");
//        if (mWeatherEvent != null){
//            showWeather(mWeatherEvent);
//        }
        mSwipeLayout.setOnRefreshListener(this);
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
        mSwipeLayout.setRefreshing(false);
        if (event.isSuccess()) {
//            mACache.put("WEATHEREVENT", event, ACache.TIME_DAY / 4);
            mWeatherEvent = event;
            mWeatherEntity = event.getWeatherEntity();
            WeatherEntity.ResultBean resultBean = mWeatherEntity.getResult().get(0);

            List<WeatherEntity.ResultBean.FutureBean> futureBeanList = resultBean.getFuture();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerWeather.setLayoutManager(linearLayoutManager);
            FutureWeatherAdapter adapter = new FutureWeatherAdapter(getActivity(), futureBeanList);
            mRecyclerWeather.setAdapter(adapter);

            EventBus.getDefault().post(resultBean);

            mCityName.setText(resultBean.getCity());
            mWeek.setText(resultBean.getWeek());
            mTemperature.setText(resultBean.getTemperature());
            mWeatherDec.setText(resultBean.getWeather());
            int drawablePath = WeatherUtil.getLikeMapPath(resultBean.getWeather());
            if (drawablePath == 0){
                drawablePath = R.drawable.w00;
            }
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

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeLayout.setRefreshing(false);
                            if (mWeatherEvent != null){
                                showWeather(mWeatherEvent);
                            }
                            long time = System.currentTimeMillis();
                            String str = MyUtil.dateYMDHM(time);
                            Toast.makeText(getActivity(), "更新成功\n" + str, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
