package com.example.mytime.event;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public class LocalEvent {
    private String province;
    private String city;

    public LocalEvent(String province, String city) {
        this.province = province;
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
