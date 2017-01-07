package com.example.mytime.mvp.ui.view;

import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.Calendar;
import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanItemView {
    /**
     * 编辑状态下展示数据
     */
    void showData(PlanItem planItem, List<Photo> photos/*, List<Time> times*/);

    void complete();

    /**
     * 设置闹钟
     * @param times
     */
    void setAlarm(Calendar calendar);

//    void cancleAlarm(List<Time> times);
}
