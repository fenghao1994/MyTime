package com.example.mytime.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.event.LocalEvent;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.IMainEntity;
import com.example.mytime.mvp.model.entity.Citys;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.model.impl.MainEntityImpl;
import com.example.mytime.mvp.presenter.IWeatherPresenter;
import com.example.mytime.mvp.presenter.impl.WeatherPresenterImpl;
import com.example.mytime.mvp.ui.activity.WeatherActivity;
import com.example.mytime.mvp.ui.adapter.CityListAdapter;
import com.example.mytime.mvp.ui.view.IWeatherView;
import com.example.mytime.util.ACache;
import com.example.mytime.util.MyUtil;
import com.example.mytime.util.WeatherUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by fenghao02 on 2017/3/27.
 */

public class CityListFragment extends Fragment implements IWeatherView , SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.list_view)
    ListView mListView;

    IWeatherPresenter weatherPresenter;
    WeatherEntity weatherEntity;
    @BindView(R.id.temperature)
    TextView mTemperature;
    @BindView(R.id.weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.weather_dec)
    TextView mWeatherDec;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private CityListAdapter mCityListAdapter;
    private List<String> mCityNameList = new ArrayList<>();
    private List<Citys.ResultBean> mResultBeenList;
    private List<Citys.ResultBean.CityBean> mCityBeanList;
    private String mCurrentProvice;
    private String mCurrentCity;

    private IMainEntity mIMainEntity;
    private WeatherEvent mWeatherEvent;

    /**
     * 列表显示等级  省为第一级，市为第二级
     */
    private int currentLevel = 1;
//    private ACache mACache;
    private Citys mCitys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        ButterKnife.bind(this, view);
//        mACache = ACache.get(getActivity());
//        mWeatherEvent = (WeatherEvent) mACache.getAsObject("WEATHEREVENT");
//        if (mWeatherEvent != null) {
//            showWeatherEvent();
//        }
//        mCitys = (Citys) mACache.getAsObject("CITYS");
        if (mCitys != null){
            showProviceAndCityList(mCitys);
        }
        mSwipeLayout.setRefreshing(true);
        mIMainEntity = new MainEntityImpl();
        Intent intent = this.getActivity().getIntent();
        mWeatherEvent = (WeatherEvent) intent.getSerializableExtra("WEATHER_EVENT");
        if (mWeatherEvent != null){
            showWeatherEvent();
        }
        weatherPresenter = new WeatherPresenterImpl(this);
//        showWeatherInfo(weatherEntity);
        getCityList();
        mSwipeLayout.setOnRefreshListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (mListView != null && mListView.getChildCount() > 0){
                    boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeLayout.setEnabled(enable);
            }
        });

        return view;
    }

    private void showWeatherEvent() {
        if(mWeatherEvent.getWeatherEntity().getResult() != null){
            showWeatherInfo(mWeatherEvent.getWeatherEntity().getResult().get(0));
        }
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

    public void getCityList() {
        weatherPresenter.getCitysList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnItemClick(R.id.list_view)
    public void onItemClick(int position) {
        if (currentLevel == 1) {
            mCurrentProvice = mResultBeenList.get(position).getProvince();
            mCityBeanList = mResultBeenList.get(position).getCity();
            mCityNameList.clear();
            for (int i = 0; i < mCityBeanList.size(); i++) {
                mCityNameList.add(mCityBeanList.get(i).getCity());
            }
            currentLevel = 2;
            mCityListAdapter.notifyDataSetChanged();
        } else if (currentLevel == 2) {
            mCurrentCity = mCityBeanList.get(position).getCity();
            LocalEvent localEvent = new LocalEvent(mCurrentProvice, mCurrentCity);
            mIMainEntity.getWeatherInfo(localEvent);
            navState();
            ((WeatherActivity) getActivity()).closeNavLayout();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @OnClick(R.id.back)
    public void onClickBack() {
        navState();
    }

    /**
     * 侧边栏状态
     */
    public void navState() {
        if (currentLevel == 1) {
            ((WeatherActivity) getActivity()).closeNavLayout();
        } else if (currentLevel == 2) {
            mCityNameList.clear();
            for (int i = 0; i < mResultBeenList.size(); i++) {
                mCityNameList.add(mResultBeenList.get(i).getProvince());
            }
            currentLevel = 1;
            mCityListAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void showWeatherInfo(WeatherEntity.ResultBean resultBean) {
        mTemperature.setText(resultBean.getTemperature());
        mTemperature.setText(resultBean.getTemperature());
        mWeatherDec.setText(resultBean.getWeather());
        int drawablePath = WeatherUtil.getLikeMapPath(resultBean.getWeather());
        if (drawablePath == 0){
            drawablePath = R.drawable.w00;
        }
        mWeatherIcon.setImageDrawable(getActivity().getResources().getDrawable(drawablePath));
    }

    @Subscribe
    public void showProviceAndCityList(Citys citys) {
        mSwipeLayout.setRefreshing(false);
        if (citys == null) {
            return;
        }
//        mACache.put("CITYS", citys, ACache.TIME_DAY * 10);
        mResultBeenList = citys.getResult();
        for (int i = 0; i < mResultBeenList.size(); i++) {
            mCityNameList.add(mResultBeenList.get(i).getProvince());
        }
        mCityListAdapter = new CityListAdapter(getActivity(), mCityNameList);
        mListView.setAdapter(mCityListAdapter);
    }

    @Override
    public void showCitysList(Citys citys) {

    }

    @Override
    public void showWeatherInfo(WeatherEntity weatherEntity) {
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeLayout.setRefreshing(false);
                            String time = MyUtil.dateYMDHM(System.currentTimeMillis());
                            Toast.makeText(getActivity(), "城市列表更新成功\n" + time, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
