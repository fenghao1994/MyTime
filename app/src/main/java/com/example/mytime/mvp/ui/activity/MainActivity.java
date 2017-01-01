package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.fragment.NoteFragment;
import com.example.mytime.mvp.ui.fragment.PlanFragment;
import com.example.mytime.mvp.ui.view.IMain;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMain {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar( toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled( true);
            actionBar.setHomeAsUpIndicator(R.drawable.celan);
        }

        if (chooseFragment == 0) {
            showPlanFragment();
        } else if (chooseFragment == 1) {
            showNoteFragment();
        }
    }

    @Override
    public void showWeather() {

    }

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

    @OnClick({R.id.plan_text, R.id.note_text, R.id.weather_layout, R.id.content_fragment_layout, R.id.activity_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_text:
                //当当前页不是展示的plan的时候才重新加载
                if (chooseFragment == 1) {
                    showPlanFragment();
                }
                break;
            case R.id.note_text:
                if (chooseFragment == 0) {
                    showNoteFragment();
                }
                break;
            case R.id.weather_layout:
                break;
            case R.id.content_fragment_layout:
                break;
            case R.id.activity_main:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()){
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
}
