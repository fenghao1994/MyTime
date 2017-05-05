package com.example.mytime.mvp.presenter.impl;

import com.example.mytime.mvp.model.ICreatePlanEntity;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.impl.CreatePlanEntityImpl;
import com.example.mytime.mvp.presenter.IAllPlanPresenter;
import com.example.mytime.mvp.ui.view.IAllPlanView;

import java.util.List;

/**
 * Created by fenghao on 2017/1/10.
 */

public class AllPlanPresenterImpl implements IAllPlanPresenter{
    private IAllPlanView allPlanView;
    private ICreatePlanEntity createPlanEntity;

    public AllPlanPresenterImpl(IAllPlanView allPlanView) {
        this.allPlanView = allPlanView;
        createPlanEntity = new CreatePlanEntityImpl();
    }

    @Override
    public void showAllPlan(boolean desc) {
        List<Plan> list = createPlanEntity.getAllPlan( desc);
        allPlanView.showAllPlan( list);
    }

    @Override
    public void showAllCompletePlan(boolean desc) {
        List<PlanItem> list = createPlanEntity.getAllCompletePlan( desc);
        allPlanView.showAllComplete( list);
    }
}
