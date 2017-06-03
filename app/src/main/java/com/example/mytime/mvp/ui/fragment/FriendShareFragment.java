package com.example.mytime.mvp.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.FriendShare;
import com.example.mytime.mvp.ui.adapter.FriendOpenAdapter;
import com.example.mytime.util.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by fenghao on 2017/6/3.
 */

public class FriendShareFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ContentResolver cr;
    private List<Map<String, String>> datalistView;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

    private SharedPreferences sp;

    private FriendOpenAdapter friendOpenAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_share, container, false);
        sp = getActivity().getSharedPreferences("MYTIME", Context.MODE_PRIVATE);

        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity());
        recyclerView.setLayoutManager( layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLianXiPeople();
    }

    /**
     * 获取联系人
     */
    public void getLianXiPeople() {
        datalistView = new ArrayList<>();
        cr = getActivity().getContentResolver();
        Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                Map<String, String> map = new HashMap<>();
                map.put("phoneNumber", phoneNumber);
                map.put("name", contactName);
                datalistView.add(map);
            }
        }
        getMessage();
    }

    /**
     * 获取朋友公开的planitem
     */
    public void getMessage() {
        String message = new Gson().toJson(datalistView);
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_FRIEND_OPEN)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("message", message)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String a = "";
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<FriendShare>>() {
                        }.getType();
                        ArrayList<FriendShare> list = gson.fromJson(response, type);
                        friendOpenAdapter = new FriendOpenAdapter(list, getActivity());
                        recyclerView.setAdapter(friendOpenAdapter);
                    }
                });
    }
}
