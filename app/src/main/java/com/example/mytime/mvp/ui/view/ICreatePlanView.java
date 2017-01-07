package com.example.mytime.mvp.ui.view;

import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.List;

/**
 * Created by fenghao on 2017/1/5.
 */

public interface ICreatePlanView {

    void showData(List<PlanItem> planItems);

    void complete();
}
