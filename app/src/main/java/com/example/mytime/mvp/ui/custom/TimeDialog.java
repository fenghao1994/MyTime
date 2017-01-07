package com.example.mytime.mvp.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mytime.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fenghao on 2017/1/7.
 */

public class TimeDialog extends Dialog {


    @BindView(R.id.setting)
    TextView setting;
    @BindView(R.id.timePicker)
    TimePicker timePicker;
    @BindView(R.id.radio_day)
    RadioButton radioDay;
    @BindView(R.id.radio_week)
    RadioButton radioWeek;
    @BindView(R.id.radio_month)
    RadioButton radioMonth;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private Context mContext;

    private int hour, min;
    private int flag = 0; // 0为默认，表示提醒一次， 1为每日，2为每周，3为每月
    //判断radiogroup是否点击过
    private boolean isClick;

    private onOkListener mOnOkListener;

    public void setOnOkListener(onOkListener mOnOkListener) {
        this.mOnOkListener = mOnOkListener;
    }


    public TimeDialog(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
    }

    public TimeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_time_dialog, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) mContext.getResources().getDisplayMetrics().widthPixels; // 宽度
        view.measure(0, 0);
        lp.height = view.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);

        timePicker.setIs24HourView(true);

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                TimeDialog.this.hour = hour;
                TimeDialog.this.min = min;
            }
        });

        radioDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isClick){
                    flag = 1;
                    isClick = true;
                }else {
                    if (flag == 1){
                        isClick = false;
                        flag = 0;
                        radioGroup.clearCheck();
                    }else {
                        flag = 1;
                    }
                }
            }
        });

        radioWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isClick){
                    flag = 2;
                    isClick = true;
                }else {
                    if (flag == 2){
                        isClick = false;
                        flag = 0;
                        radioGroup.clearCheck();
                    }else {
                        flag = 2;
                    }
                }
            }
        });

        radioMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !isClick){
                    flag = 3;
                    isClick = true;
                }else {
                    if (flag == 3){
                        isClick = false;
                        flag = 0;
                        radioGroup.clearCheck();
                    }else {
                        flag = 3;
                    }
                }
            }
        });


    }

    @OnClick(R.id.setting)
    public void onClick() {
        mOnOkListener.getTime(hour, min, flag);
    }

    public interface onOkListener {
        void getTime(int hour, int min, int flag);
    }
}
