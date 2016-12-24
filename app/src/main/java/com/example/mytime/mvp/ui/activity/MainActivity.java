package com.example.mytime.mvp.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.view.IMain;

public class MainActivity extends AppCompatActivity implements IMain {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void showWeather() {

    }

    @Override
    public void clickWeather() {

    }

    @Override
    public void showPlanFragment() {

    }

    @Override
    public void showNoteFragment() {

    }

    @Override
    public void clickFab() {

    }
}
