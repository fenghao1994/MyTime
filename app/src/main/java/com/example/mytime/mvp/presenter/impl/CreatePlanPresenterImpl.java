package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.ICreatePlanEntity;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.impl.CreatePlanEntityImpl;
import com.example.mytime.mvp.presenter.ICreatePlanPresenter;
import com.example.mytime.mvp.ui.view.ICreatePlanView;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreatePlanPresenterImpl implements ICreatePlanPresenter {

    private ICreatePlanView createPlanView;
    private ICreatePlanEntity createPlanEntity;

    public CreatePlanPresenterImpl(ICreatePlanView createPlanView) {
        this.createPlanView = createPlanView;
        this.createPlanEntity = new CreatePlanEntityImpl();
    }

    @Override
    public void showData(Plan plan) {
        List<PlanItem> list = createPlanEntity.getPlanItems( plan);
        createPlanView.showData( list);
    }

    @Override
    public void savePlan(Plan plan) {
        createPlanEntity.savePlan( plan);
        createPlanView.complete();
    }

    @Override
    public void updatePlan(Plan plan) {
        createPlanEntity.updatePlan( plan);
        createPlanView.complete();
    }
}
