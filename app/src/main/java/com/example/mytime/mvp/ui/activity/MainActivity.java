package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.event.LocalEvent;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.presenter.IMainPresenter;
import com.example.mytime.mvp.presenter.impl.MainPresenterImpl;
import com.example.mytime.mvp.ui.fragment.NoteFragment;
import com.example.mytime.mvp.ui.fragment.PlanFragment;
import com.example.mytime.mvp.ui.view.IMainView;
import com.example.mytime.service.LocalService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final String TAG = "MainActivity";

    @BindView(R.id.plan_text)
    TextView planText;
    @BindView(R.id.note_text)
    TextView noteText;
    @BindView(R.id.weather_layout)
    RelativeLayout weatherLayout;
    @BindView(R.id.content_fragment_layout)
    FrameLayout contentFragmentLayout;
    @BindView(R.id.activity_main)
    DrawerLayout activityMain;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //plan和note的选择 0为plan 、1为note
    private int chooseFragment;

    private Fragment planFragment;
    private Fragment noteFragment;

    private IMainPresenter mainPresenter;
    private WeatherEntity weatherEntity;

    private boolean isFirstShowWeatherInfo = true;

    private LocalEvent mLocalEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.celan);
        }
        initLabel();

        if (chooseFragment == 0) {
            showPlanFragment();
        } else if (chooseFragment == 1) {
            showNoteFragment();
        }

        Intent intent = new Intent(this, LocalService.class);
        startService(intent);
        EventBus.getDefault().register(this);

        mainPresenter = new MainPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册event

        if (mLocalEvent != null){
            mainPresenter.getWeatherInfo(mLocalEvent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Subscribe
    public void onLocalEvent(LocalEvent localEvent) {
        if (isFirstShowWeatherInfo) {
            mLocalEvent = localEvent;
//            getWeatherInfo(localEvent);
            mainPresenter.getWeatherInfo(localEvent);
            isFirstShowWeatherInfo = false;
        }

    }

//    public void getWeatherInfo(LocalEvent localEvent) {
//
//    }


    @Override
    public void clickWeather() {

    }

    @Override
    public void showPlanFragment() {
        chooseFragment = 0;
        planFragment = new PlanFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment_layout, planFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showNoteFragment() {
        chooseFragment = 1;
        noteFragment = new NoteFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment_layout, noteFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void clickFab() {

    }

    @Override
    public void showWeather(WeatherEntity entity) {

    }

    //// TODO: 2017/3/28  weather数据加载
    @Subscribe
    public void showWeathers(WeatherEvent entity) {
        if ( entity.isSuccess()){
            this.weatherEntity = entity.getWeatherEntity();
        }else {
            //加载默认的
        }
        Log.i("WeatherInfoFragment", "clf ---- 》>>>>> " + entity.toString());

    }

    private void initLabel(){
        if ( chooseFragment == 0){
            planText.setBackgroundColor(this.getResources().getColor(R.color.logoGreen));
            noteText.setBackgroundColor(this.getResources().getColor(R.color.darkGray));
        }else {
            noteText.setBackgroundColor(this.getResources().getColor(R.color.logoGreen));
            planText.setBackgroundColor(this.getResources().getColor(R.color.darkGray));
        }
    }


    @OnClick({R.id.plan_text, R.id.note_text, R.id.weather_layout, R.id.content_fragment_layout, R.id.activity_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_text:
                //当当前页不是展示的plan的时候才重新加载
                if (chooseFragment == 1) {
                    showPlanFragment();
                    initLabel();
                }
                break;
            case R.id.note_text:
                if (chooseFragment == 0) {
                    showNoteFragment();
                    initLabel();
                }
                break;
            case R.id.weather_layout:
                gotoWeatherActivity();
                break;
            case R.id.content_fragment_layout:
                break;
            case R.id.activity_main:
                break;
        }
    }

    public void gotoWeatherActivity() {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("WEATHER", weatherEntity);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showNavLayout();
                break;
        }

        return true;
    }

    //打开侧栏
    private void showNavLayout() {
        activityMain.openDrawer(GravityCompat.START);
    }

    //关闭侧栏
    private void closeNavLayout() {
        activityMain.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
