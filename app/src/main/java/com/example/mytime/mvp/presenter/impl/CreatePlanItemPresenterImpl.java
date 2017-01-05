package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.ICreatePlanItemEntity;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.Time;
import com.example.mytime.mvp.model.impl.CreatePlanItemEntityImpl;
import com.example.mytime.mvp.presenter.ICreatePlanItemPresenter;
import com.example.mytime.mvp.ui.view.ICreatePlanItemView;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreatePlanItemPresenterImpl implements ICreatePlanItemPresenter {
    private ICreatePlanItemView createPlanItemView;
    private ICreatePlanItemEntity createPlanItemEntity;

    public CreatePlanItemPresenterImpl(ICreatePlanItemView iCreatePlanItemView){
        this.createPlanItemView = iCreatePlanItemView;
        createPlanItemEntity = new CreatePlanItemEntityImpl();
    }


    @Override
    public void savePlanItem(PlanItem planItem, List<Photo> photos, List<Time> times) {
        createPlanItemEntity.saveNote( planItem, photos, times);
        createPlanItemView.setAlarm( times);
        createPlanItemView.complete();
    }

    @Override
    public void updatePlanItem(PlanItem planItem, List<Photo> photos, List<Time> times) {
        List<Time> oldTimes = createPlanItemEntity.getTimes( planItem);
        createPlanItemEntity.updatePlanItem( planItem, photos, times);
        createPlanItemView.cancleAlarm( oldTimes);
        createPlanItemView.setAlarm( times);
        createPlanItemView.complete();
    }

    @Override
    public void showData(PlanItem planItem) {
        List<Photo> photos = createPlanItemEntity.getPhotoAddress( planItem);
        List<Time> times = createPlanItemEntity.getTimes( planItem);
        createPlanItemView.showData( planItem, photos, times);
    }
}
