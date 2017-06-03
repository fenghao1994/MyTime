package com.example.mytime.mvp.model.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fenghao on 2017/6/3.
 *
 */

public class FriendShare implements Serializable {
    PlanItem planItem;
    User user;

    public PlanItem getPlanItem() {
        return planItem;
    }

    public void setPlanItem(PlanItem planItem) {
        this.planItem = planItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
