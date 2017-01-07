package com.example.mytime.mvp.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.mytime.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fenghao on 2017/1/4.
 * 自定义事件选择器
 */

public class DateDialog extends Dialog {
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

    private Context mContext;

    private onOkListener mOnOkListener;

    private int year, month, day;

    public DateDialog(Context context) {
        this(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;

    }

    public DateDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.layout_date_dialog, null);
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
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendarView.state().edit()
                .setMinimumDate(calendar)
                .setMaximumDate(CalendarDay.from(year, month, day))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DateDialog.this.year = date.getYear();
                DateDialog.this.month = date.getMonth() + 1;
                DateDialog.this.day = date.getDay();
            }
        });

    }

    public void setOnOkListener(onOkListener mOnOkListener) {
        this.mOnOkListener = mOnOkListener;
    }


    public void onClick() {
        mOnOkListener.getDate(year, month ,day);
    }

    @OnClick({R.id.radio_every_day, R.id.radio_every_week, R.id.radio_every_month, R.id.layout_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_every_day:
                break;
            case R.id.radio_every_week:
                break;
            case R.id.radio_every_month:
                break;
            case R.id.layout_next:
                onClick();
                break;
        }
    }

    public interface onOkListener {
        void getDate(int year, int month, int day);
    }

}
