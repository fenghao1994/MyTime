package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.ICreatePlanItemEntity;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.impl.CreatePlanItemEntityImpl;
import com.example.mytime.mvp.presenter.ICreatePlanItemPresenter;
import com.example.mytime.mvp.ui.view.ICreatePlanItemView;

import java.util.Calendar;
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
    public void savePlanItem(PlanItem planItem, List<Photo> photos) {
        createPlanItemEntity.savePlanItem( planItem, photos);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, planItem.getYear());
        calendar.set(Calendar.MONTH, planItem.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, planItem.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, planItem.getHour());
        calendar.set(Calendar.MINUTE, planItem.getMinute());
        calendar.set(Calendar.MILLISECOND, 0);
        createPlanItemView.setAlarm( calendar);
        createPlanItemView.complete();
    }

    @Override
    public void updatePlanItem(PlanItem planItem, List<Photo> photos) {
        createPlanItemEntity.updatePlanItem( planItem, photos);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, planItem.getYear());
        calendar.set(Calendar.MONTH, planItem.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, planItem.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, planItem.getHour());
        calendar.set(Calendar.MINUTE, planItem.getMinute());
        calendar.set(Calendar.MILLISECOND, 0);
        createPlanItemView.setAlarm( calendar);
        createPlanItemView.complete();
    }

    @Override
    public void showData(PlanItem planItem) {
        List<Photo> photos = createPlanItemEntity.getPhotoAddress( planItem);
        createPlanItemView.showData( planItem, photos);
    }
}
