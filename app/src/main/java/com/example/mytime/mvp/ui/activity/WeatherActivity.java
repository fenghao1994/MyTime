package com.example.mytime.mvp.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.mytime.R;
import com.lzy.imagepicker.view.SystemBarTintManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity {


    @BindView(R.id.activity_weath)
    DrawerLayout activityWeath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weath);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // create our manager instance after the content view is set

        }
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
