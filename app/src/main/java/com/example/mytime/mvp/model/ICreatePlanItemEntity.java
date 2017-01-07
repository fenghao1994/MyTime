package com.example.mytime.mvp.model;

import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanItemEntity {
    /**
     * @param planItem
     * @param photos
     */
    void savePlanItem(PlanItem planItem, List<Photo> photos);

    /**
     *
     * @param planItem
     * @param photos
     */
    void updatePlanItem(PlanItem planItem, List<Photo> photos);

    List<Photo> getPhotoAddress(PlanItem planItem);

}
