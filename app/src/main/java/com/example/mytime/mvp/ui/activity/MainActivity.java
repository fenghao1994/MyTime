package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.event.LocalEvent;
import com.example.mytime.event.WeatherEvent;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.WeatherEntity;
import com.example.mytime.mvp.presenter.IMainPresenter;
import com.example.mytime.mvp.presenter.impl.MainPresenterImpl;
import com.example.mytime.mvp.ui.fragment.FriendShareFragment;
import com.example.mytime.mvp.ui.fragment.MineFragment;
import com.example.mytime.mvp.ui.fragment.NoteFragment;
import com.example.mytime.mvp.ui.fragment.PlanFragment;
import com.example.mytime.mvp.ui.view.IMainView;
import com.example.mytime.service.LocalService;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.example.mytime.util.WeatherUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements IMainView, SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.temperature)
    TextView mTemperature;
    @BindView(R.id.weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.weather_dec)
    TextView mWeatherDec;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.bottom_tab_zhuye_img)
    ImageView bottomTabZhuyeImg;
    @BindView(R.id.bottom_tab_zhuye_text)
    TextView bottomTabZhuyeText;
    @BindView(R.id.bottom_tab_zhuye_layout)
    LinearLayout bottomTabZhuyeLayout;
    @BindView(R.id.bottom_tab_friend_share_img)
    ImageView bottomTabFriendShareImg;
    @BindView(R.id.bottom_tab_friend_share_text)
    TextView bottomTabFriendShareText;
    @BindView(R.id.bottom_tab_friend_share_layout)
    LinearLayout bottomTabFriendShareLayout;
    @BindView(R.id.bottom_tab_mine_img)
    ImageView bottomTabMineImg;
    @BindView(R.id.bottom_tab_mine_text)
    TextView bottomTabMineText;
    @BindView(R.id.bottom_tab_mine_layout)
    LinearLayout bottomTabMineLayout;

    //plan和note的选择 0为plan 、1为note
    private int chooseFragment;

    private Fragment planFragment;
    private Fragment noteFragment;

    private IMainPresenter mainPresenter;
    private WeatherEntity weatherEntity;

    private boolean isFirstShowWeatherInfo = true;

    private LocalEvent mLocalEvent;
    private WeatherEvent mWeatherEvent;

    private SharedPreferences sp;

    private FriendShareFragment friendShareFragment;
    private MineFragment mineFragment;

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

        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(this);

        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);


        //发送刷新widget广播
        Intent widget = new Intent(Extra.WIDGET_TIME);
        this.sendBroadcast(widget);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //注册event

        if (mLocalEvent != null) {
            mainPresenter.getWeatherInfo(mLocalEvent);
        }
        if (weatherEntity != null) {
            displayWeather(weatherEntity);
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

        Extra.entity = entity.getWeatherEntity();

        mWeatherEvent = entity;
        if (entity.isSuccess()) {
            this.weatherEntity = entity.getWeatherEntity();
            displayWeather(weatherEntity);
        } else {
            //加载默认的
            Toast.makeText(this, "天气获取失败", Toast.LENGTH_SHORT).show();
        }
        mSwipeLayout.setRefreshing(false);
        Log.i("WeatherInfoFragment", "clf ---- 》>>>>> " + entity.toString());
    }

    public void displayWeather(WeatherEntity weatherEntity) {
        WeatherEntity.ResultBean resultBean = weatherEntity.getResult().get(0);
        mTemperature.setText(resultBean.getTemperature());
        mTemperature.setText(resultBean.getTemperature());
        mWeatherDec.setText(resultBean.getWeather());
        int drawablePath = WeatherUtil.getLikeMapPath(resultBean.getWeather());
        if (drawablePath == 0) {
            drawablePath = R.drawable.w00;
        }
        mWeatherIcon.setImageDrawable(this.getResources().getDrawable(drawablePath));
    }

    private void initLabel() {
        initBottomLayout();
        bottomTabZhuyeImg.setImageDrawable(getResources().getDrawable(R.drawable.zhuye));
        bottomTabZhuyeText.setTextColor(getResources().getColor(R.color.logoGreen));
        if (chooseFragment == 0) {
            planText.setBackgroundColor(this.getResources().getColor(R.color.logoGreen));
            noteText.setBackgroundColor(this.getResources().getColor(R.color.darkGray));
        } else {
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
        intent.putExtra("WEATHER_EVENT", mWeatherEvent);
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
                            long time = System.currentTimeMillis();
                            String str = MyUtil.dateYMDHM(time);
                            Toast.makeText(MainActivity.this, "更新成功\n" + str, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteNote(Note note) {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_DELETE_NOTE)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("id", note.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("MYTIME_OKHTTP", "删除成功");
                    }
                });
    }


    @OnClick(R.id.bottom_tab_zhuye_layout)
    public void zhuyeClick(){
        initBottomLayout();
        bottomTabZhuyeImg.setImageDrawable(getResources().getDrawable(R.drawable.zhuye));
        bottomTabZhuyeText.setTextColor(getResources().getColor(R.color.logoGreen));
        if (chooseFragment == 0) {
            showPlanFragment();
        } else if (chooseFragment == 1) {
            showNoteFragment();
        }
    }

    @OnClick(R.id.bottom_tab_friend_share_layout)
    public void friendShareClick(){
        initBottomLayout();
        bottomTabFriendShareImg.setImageDrawable(getResources().getDrawable(R.drawable.friend_share));
        bottomTabFriendShareText.setTextColor(getResources().getColor(R.color.logoGreen));
        friendShareFragment = new FriendShareFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment_layout, friendShareFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.bottom_tab_mine_layout)
    public void mineClick(){
        Intent intent = new Intent(this, MineActivity.class);
        startActivity(intent);
//        initBottomLayout();
//        bottomTabMineImg.setImageDrawable(getResources().getDrawable(R.drawable.mine));
//        bottomTabMineText.setTextColor(getResources().getColor(R.color.logoGreen));
//        mineFragment = new MineFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.content_fragment_layout, mineFragment);
//        fragmentTransaction.commit();
    }

    /**
     * 初始化底部layout  全部变为灰色
     */
    public void initBottomLayout(){
        bottomTabZhuyeImg.setImageDrawable(getResources().getDrawable(R.drawable.zhuye_gray));
        bottomTabZhuyeText.setTextColor(getResources().getColor(R.color.mainTextColor));
        bottomTabFriendShareImg.setImageDrawable(getResources().getDrawable(R.drawable.friend_share_gray));
        bottomTabFriendShareText.setTextColor(getResources().getColor(R.color.mainTextColor));
        bottomTabMineImg.setImageDrawable(getResources().getDrawable(R.drawable.mine_gray));
        bottomTabMineText.setTextColor(getResources().getColor(R.color.mainTextColor));
    }

}
