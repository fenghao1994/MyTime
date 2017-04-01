package com.example.mytime.mvp.ui.custom;

import java.util.List;

/**
 * Created by fenghao02 on 2017/4/1.
 */

public class ChartModel {
    //横轴数据
    private List<Integer> horizontalAxis;

    //纵轴数据
    private List<Integer>  verticalAxis;

    public ChartModel(List<Integer> horizontalAxis, List<Integer> verticalAxis) {
        this.horizontalAxis = horizontalAxis;
        this.verticalAxis = verticalAxis;
    }

    public List<Integer> getHorizontalAxis() {
        return horizontalAxis;
    }

    public void setHorizontalAxis(List<Integer> horizontalAxis) {
        this.horizontalAxis = horizontalAxis;
    }

    public List<Integer> getVerticalAxis() {
        return verticalAxis;
    }

    public void setVerticalAxis(List<Integer> verticalAxis) {
        this.verticalAxis = verticalAxis;
    }
}
