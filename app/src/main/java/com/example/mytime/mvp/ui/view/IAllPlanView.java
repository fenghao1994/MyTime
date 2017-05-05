package com.example.mytime.mvp.ui.view;

import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.List;

/**
 * Created by fenghao on 2017/1/10.
 */

public interface IAllPlanView {
    void showAllPlan(List<Plan> list);

    void showAllComplete(List<PlanItem> list);
}
