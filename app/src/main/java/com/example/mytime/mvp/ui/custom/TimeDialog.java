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
import android.widget.RelativeLayout;

import com.example.mytime.R;
import com.example.mytime.mvp.ui.view.ITimeView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
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
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.radio_every_day)
    RadioButton radioEveryDay;
    @BindView(R.id.radio_every_week)
    RadioButton radioEveryWeek;
    @BindView(R.id.radio_every_month)
    RadioButton radioEveryMonth;
    @BindView(R.id.layout_next)
    RelativeLayout layoutNext;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    //单选按钮的值  默认为0，每天为1，每周为2，每月为3
    private int flag;
    //单选是否被点击，重复点击重置为false
    private boolean isClick;

    private Context mContext;

    private onOkListener mOnOkListener;

    public TimeDialog(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;

    }

    public TimeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }


    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_dialog, null);
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
        //// TODO: 2017/1/7  调整calendar的大小  通过设置mesure
        Calendar calendar = Calendar.getInstance();

        calendarView.setDateSelected(calendar, true);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendarView.state().edit()
                .setMinimumDate(calendar)
                .setMaximumDate(CalendarDay.from(year, month, day))
                .commit();

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


    public void onClick() {
        isEveryDaySetting = false;
        settingTimes = new ArrayList<>();
        long t = System.currentTimeMillis() + 1000 * 60;
        settingTimes.add(t);
        mOnOkListener.getDate(isEveryDaySetting, settingTimes);
    }

    @OnClick({R.id.radio_every_day, R.id.radio_every_week, R.id.radio_every_month, R.id.layout_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_every_day:
                flag = 1;

                break;
            case R.id.radio_every_week:
                flag = 2;
                isClick = true;
                break;
            case R.id.radio_every_month:
                flag = 3;
                isClick = true;
                break;
            case R.id.layout_next:
                onClick();
                break;
        }
    }

   /* public void reset() {
        if (!isClick && flag != 0) {
            //// TODO: 2017/1/7  之前没点击
            isClick = true;
        } else {
            //// TODO: 2017/1/7 之前点击过，现在取消
            isClick = false;
            flag = 0;
        }

        if (flag == 0) {
            radioGroup.clearCheck();
        }
    }*/

    public interface onOkListener {
        void getDate(boolean isEveryDaySetting, List<Long> settingTimes);
    }

}
