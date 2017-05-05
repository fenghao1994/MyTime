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

    /**
     * 获取所有的plan
     * @param desc 是否倒序
     */
    List<Plan> getAllPlan(boolean desc);

    /**
     * 已经完成的plan
     * @param desc
     * @return
     */
    List<PlanItem> getAllCompletePlan(boolean desc);
}
