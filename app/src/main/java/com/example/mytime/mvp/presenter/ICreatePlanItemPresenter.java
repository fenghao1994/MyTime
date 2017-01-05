package com.example.mytime.mvp.presenter;

import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.Time;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanItemPresenter {
    /**
     * @param planItem
     * @param photos
     * @param times
     */
    void savePlanItem(PlanItem planItem, List<Photo> photos, List<Time> times);

    /**
     *
     * @param planItem
     * @param photos
     * @param times
     */
    void updatePlanItem(PlanItem planItem, List<Photo> photos, List<Time> times);

    void showData( PlanItem planItem);
}
