package com.example.mytime.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mytime.R;
import com.example.mytime.event.LocalEvent;
import com.example.mytime.mvp.model.IMainEntity;
import com.example.mytime.mvp.model.entity.Citys;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.model.impl.MainEntityImpl;
import com.example.mytime.mvp.presenter.IWeatherPresenter;
import com.example.mytime.mvp.presenter.impl.WeatherPresenterImpl;
import com.example.mytime.mvp.ui.activity.WeatherActivity;
import com.example.mytime.mvp.ui.adapter.CityListAdapter;
import com.example.mytime.mvp.ui.view.IWeatherView;

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

public class CityListFragment extends Fragment implements IWeatherView {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.list_view)
    ListView mListView;

    IWeatherPresenter weatherPresenter;
    WeatherEntity weatherEntity;

    private CityListAdapter mCityListAdapter;
    private List<String> mCityNameList = new ArrayList<>();
    private List<Citys.ResultBean> mResultBeenList;
    private List<Citys.ResultBean.CityBean> mCityBeanList;
    private String mCurrentProvice;
    private String mCurrentCity;

    private IMainEntity mIMainEntity;

    /**
     * 列表显示等级  省为第一级，市为第二级
     */
    private int currentLevel = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        ButterKnife.bind(this, view);

        mIMainEntity = new MainEntityImpl();
        Intent intent = this.getActivity().getIntent();
        weatherEntity = (WeatherEntity) intent.getSerializableExtra("WEATHER");
        weatherPresenter = new WeatherPresenterImpl(this);
//        showWeatherInfo(weatherEntity);
        getCityList();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register( this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister( this);
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
        if( currentLevel == 1){
            mCurrentProvice = mResultBeenList.get( position).getProvince();
            mCityBeanList = mResultBeenList.get( position).getCity();
            mCityNameList.clear();
            for (int i = 0; i < mCityBeanList.size(); i++){
                mCityNameList.add( mCityBeanList.get(i).getCity());
            }
            currentLevel = 2;
            mCityListAdapter.notifyDataSetChanged();
        }else if (currentLevel == 2){
            mCurrentCity = mCityBeanList.get( position).getCity();
            LocalEvent localEvent = new LocalEvent(mCurrentProvice, mCurrentCity);
            mIMainEntity.getWeatherInfo( localEvent);
            navState();
            ((WeatherActivity)getActivity()).closeNavLayout();

        }
    }

    @OnClick(R.id.back)
    public void onClickBack(){
        navState();
    }

    /**
     * 侧边栏状态
     */
    public void navState(){
        if ( currentLevel == 1){
            ((WeatherActivity)getActivity()).closeNavLayout();
        }else if (currentLevel == 2){
            mCityNameList.clear();
            for (int i = 0 ;i < mResultBeenList.size(); i++){
                mCityNameList.add(mResultBeenList.get(i).getProvince());
            }
            currentLevel = 1;
            mCityListAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void showProviceAndCityList(Citys citys){
        if ( citys == null){
            return;
        }
        mResultBeenList = citys.getResult();
        for (int i = 0; i < mResultBeenList.size(); i++) {
            mCityNameList.add(mResultBeenList.get(i).getProvince());
        }
        mCityListAdapter = new CityListAdapter(getActivity(), mCityNameList);
        mListView.setAdapter( mCityListAdapter);
    }

    @Override
    public void showCitysList(Citys citys) {

    }

    @Override
    public void showWeatherInfo(WeatherEntity weatherEntity) {
    }
}
