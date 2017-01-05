package com.example.mytime.mvp.ui.view;

import java.util.List;

/**
 * Created by fenghao on 2017/1/4.
 * 时间选择dialog
 */

public interface ITimeView {
    //是否设置为每天提醒
    boolean isEveryDay();

    /**
     * 得到所设置的时间（包括年月日，时分）
     * 可以选择每天也可以选择多天
     * @return
     */
    List<Long> getSettingTime();
}
