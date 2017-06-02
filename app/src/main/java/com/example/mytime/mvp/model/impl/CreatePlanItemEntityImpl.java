package com.example.mytime.mvp.model.impl;

import com.example.mytime.mvp.model.ICreatePlanItemEntity;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreatePlanItemEntityImpl implements ICreatePlanItemEntity {
    @Override
    public void savePlanItem(PlanItem planItem, List<Photo> photos) {
        planItem.save();
        planItem = DataSupport.where("createTime = ?", planItem.getCreateTime() + "").findFirst( PlanItem.class);
        if (photos != null && photos.size() > 0){
            for ( int i = 0; i < photos.size(); i++){
                photos.get(i).setObjectType(1);
                photos.get(i).setObjectId( planItem.getId());
                photos.get(i).setCreateTime(planItem.getCreateTime());
                photos.get(i).save();
            }
        }
    }

    @Override
    public void updatePlanItem(PlanItem planItem, List<Photo> photos) {
        planItem.update(planItem.getId());
        if (photos != null) {
            DataSupport.deleteAll(Photo.class, "objectType = ? and createTime = ?", 1 + "", planItem.getCreateTime() + "");
            for (int i = 0; i < photos.size(); i++) {
                Photo photo = new Photo();
                photo.setObjectId(planItem.getId());
                photo.setAddress(photos.get(i).getAddress());
                photo.setObjectType(1);
                photo.setCreateTime(planItem.getCreateTime());
                photo.save();
            }
        }
    }

    @Override
    public List<Photo> getPhotoAddress(PlanItem planItem) {
        return DataSupport.where("objectType = ? and createTime = ?", "1", planItem.getCreateTime() + "").find(Photo.class);

    }

    @Override
    public PlanItem getPlanItem(long createTime) {
        List<PlanItem> planItems = DataSupport.where("createTime = ?", createTime + "").find(PlanItem.class);
        if (planItems != null){
            return planItems.get(0);
        }
        return null;
    }
}
