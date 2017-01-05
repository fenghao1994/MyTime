package com.example.mytime.mvp.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.view.ITimeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fenghao on 2017/1/4.
 * 自定义事件选择器
 */

public class TimeDialog extends Dialog implements ITimeView {
    //是否每日提醒
    boolean isEveryDaySetting;
    //设置的时间
    ArrayList<Long> settingTimes;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.time)
    EditText time;
    @BindView(R.id.ok)
    Button ok;


    private Context mContext;

    private onOkListener mOnOkListener;

    public TimeDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_dialog, null);
        setContentView(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public boolean isEveryDay() {
        return false;
    }

    @Override
    public List<Long> getSettingTime() {
        return settingTimes;
    }

    public void setOnOkListener(onOkListener mOnOkListener) {
        this.mOnOkListener = mOnOkListener;
    }


    @OnClick(R.id.ok)
    public void onClick() {
        isEveryDaySetting = false;
        settingTimes = new ArrayList<>();
        long t = System.currentTimeMillis() + 1000 * 60;
        settingTimes.add(t);
        mOnOkListener.getDate(isEveryDaySetting, settingTimes);
    }

    public interface onOkListener {
        void getDate(boolean isEveryDaySetting, List<Long> settingTimes);
    }

}
