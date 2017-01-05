package com.example.mytime.mvp.model;

import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.Time;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanItemEntity {
    /**
     * @param planItem
     * @param photos
     * @param times
     */
    void saveNote(PlanItem planItem, List<Photo> photos, List<Time> times);

    /**
     *
     * @param planItem
     * @param photos
     * @param times
     */
    void updatePlanItem(PlanItem planItem, List<Photo> photos, List<Time> times);

    List<Photo> getPhotoAddress(PlanItem planItem);

    List<Time> getTimes(PlanItem planItem);
}
