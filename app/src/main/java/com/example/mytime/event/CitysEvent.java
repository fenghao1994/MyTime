package com.example.mytime.event;

import com.example.mytime.mvp.model.entity.Citys;

/**
 * Created by fenghao02 on 2017/3/27.
 */

public class CitysEvent {
    private Citys mCitys;

    public CitysEvent(Citys citys) {
        mCitys = citys;
    }

    public Citys getCitys() {
        return mCitys;
    }

    public void setCitys(Citys citys) {
        mCitys = citys;
    }
}
