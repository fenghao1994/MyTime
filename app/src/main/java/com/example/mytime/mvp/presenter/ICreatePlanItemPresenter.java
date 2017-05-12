package com.example.mytime.mvp.presenter;

import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanItemPresenter {
    /**
     * @param planItem
     * @param photos
     */
    void savePlanItem(PlanItem planItem, List<Photo> photos/*, List<Time> times*/);

    /**
     *
     * @param planItem
     * @param photos
     */
    void updatePlanItem(PlanItem planItem, List<Photo> photos/*, List<Time> times*/);

    void showData( PlanItem planItem);
    /**
     * 网络情况下
     * 本地获得存储后的note
     * @param createTime
     * @return
     */
    PlanItem getPlanItem(long createTime);

    List<Photo> getPhoto(PlanItem planItem);
}
