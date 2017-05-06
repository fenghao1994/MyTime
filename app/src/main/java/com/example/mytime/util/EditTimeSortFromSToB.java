package com.example.mytime.util;

import com.example.mytime.mvp.model.entity.PlanItem;

import java.util.Comparator;

/**
 * Created by fenghao on 2017/5/6.
 */

public class EditTimeSortFromSToB implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        PlanItem planItem1 = (PlanItem) o1;
        PlanItem planItem2 = (PlanItem) o2;
        if (planItem1.getEditTime() < planItem2.getEditTime()){
            return 0;
        }
        return 1;
    }
}
