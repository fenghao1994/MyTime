package com.example.mytime.http_callback;

import com.example.mytime.mvp.model.entity.User;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by fenghao on 2017/5/7.
 */

public abstract class UserCallBack extends Callback<User> {
    @Override
    public User parseNetworkResponse(Response response, int id) throws Exception {
        String str = response.body().string();
        User user = new Gson().fromJson(str, User.class);
        return null;
    }
}
