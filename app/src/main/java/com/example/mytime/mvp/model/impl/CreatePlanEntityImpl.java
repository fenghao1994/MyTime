package com.example.mytime.mvp.model.impl;

import com.example.mytime.mvp.model.ICreatePlanEntity;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public class CreatePlanEntityImpl implements ICreatePlanEntity {
    @Override
    public void savePlan(Plan plan) {
        plan.save();
    }

    @Override
    public void updatePlan(Plan plan) {
        plan.update( plan.getId());
    }

    @Override
    public List<PlanItem> getPlanItems(Plan plan) {

        return DataSupport.where("planId = ?", plan.getPlanId() + "").find(PlanItem.class);
    }
}
