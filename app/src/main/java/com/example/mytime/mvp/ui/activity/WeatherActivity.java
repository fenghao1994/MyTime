package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {


    @BindView(R.id.weather_info)
    TextView weatherInfo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_weath)
    DrawerLayout activityWeath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weath);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.celan);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("HOME", "home click");
                showNavLayout();
                break;
        }
        return true;
    }

    //打开侧栏
    public void showNavLayout() {
        activityWeath.openDrawer(GravityCompat.START);
    }

    //关闭侧栏
    public void closeNavLayout() {
        activityWeath.closeDrawer(GravityCompat.START);
    }
}
