package com.example.mytime.mvp.model;

import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanEntity {

    void savePlan(Plan plan);

    void updatePlan(Plan plan);

    List<PlanItem> getPlanItems(Plan plan);
}
