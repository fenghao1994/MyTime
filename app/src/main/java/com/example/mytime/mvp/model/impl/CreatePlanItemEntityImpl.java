package com.example.mytime.mvp.model.impl;

import com.example.mytime.mvp.model.ICreatePlanItemEntity;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.Time;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreatePlanItemEntityImpl implements ICreatePlanItemEntity {
    @Override
    public void saveNote(PlanItem planItem, List<Photo> photos, List<Time> times) {
        planItem.save();
        PlanItem currentPlanItem = DataSupport.where("createTime = ?", planItem.getCreateTime() + "").findFirst( PlanItem.class);
        if (times != null && times.size() > 0){
            for ( int i = 0; i < times.size() ; i++){
                times.get(i).setPlanItemId( currentPlanItem.getId());
                times.get(i).save();
            }
        }
        if (photos != null && photos.size() > 0){
            for ( int i = 0; i < photos.size(); i++){
                photos.get(i).setObjectType(1);
                photos.get(i).setObjectId( currentPlanItem.getId());
                photos.get(i).save();
            }
        }
    }

    @Override
    public void updatePlanItem(PlanItem planItem, List<Photo> photos, List<Time> times) {
        planItem.update(planItem.getId());
        DataSupport.deleteAll(Time.class, "planItemId = ?", planItem.getPlanId() + "");
        if (times != null && times.size() > 0) {
            for (int i = 0; i < times.size(); i++) {
                times.get(i).setPlanItemId(planItem.getId());
                times.get(i).save();
            }
        }
        if (photos != null) {
            DataSupport.deleteAll(Photo.class, "objectType = ? and objectId = ?", 1 + "", planItem.getId() + "");
            for (int i = 0; i < photos.size(); i++) {
                photos.get(i).setObjectType(1);
                photos.get(i).setObjectId(planItem.getId());
                photos.get(i).save();
            }
        }
    }

    @Override
    public List<Photo> getPhotoAddress(PlanItem planItem) {
        return DataSupport.where("objectType = ? and objectId = ?", "1", planItem.getId() + "").find(Photo.class);

    }

    @Override
    public List<Time> getTimes(PlanItem planItem) {
        return DataSupport.where("planItemId = ?", planItem.getId() + "").find( Time.class);
    }
}
