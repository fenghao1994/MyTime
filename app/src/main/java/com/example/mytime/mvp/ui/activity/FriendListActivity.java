package com.example.mytime.mvp.ui.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Friend;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.ui.adapter.FriendListAdapter;
import com.example.mytime.util.HttpUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FriendListActivity extends AppCompatActivity implements FriendListAdapter.OnChangeRelationShip {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    private User user;
    private ContentResolver cr;
    private List<Map<String, String>> datalistView;
    private FriendListAdapter friendListAdapter;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        user = DataSupport.findFirst(User.class);
        getLianXiPeople();
    }

    /**
     * 获取联系人
     */
    public void getLianXiPeople() {
        datalistView = new ArrayList<>();
        cr = getContentResolver();
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
     * 获取朋友列表
     */
    public void getMessage() {
        refresh.setRefreshing(true);
        String message = new Gson().toJson(datalistView);
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_FRIEND_LIST)
                .addParams("phoneNumber", user.getPhoneNumber())
                .addParams("message", message)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String a = "";
                        refresh.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Friend>>() {
                        }.getType();
                        ArrayList<Friend> list = gson.fromJson(response, type);
                        friendListAdapter = new FriendListAdapter(list, FriendListActivity.this);
                        friendListAdapter.setOnChangeRelationShip(FriendListActivity.this);
                        recyclerView.setAdapter(friendListAdapter);
                        refresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    @Override
    public void onChangeRelationShip(Friend friend) {
        sendMessgeRelationship(user.getPhoneNumber(), friend.getUser().getPhoneNumber(), friend.getUserActive());
    }

    /**
     * 向服务器发送关系变化数据
     */
    public void sendMessgeRelationship(String ownPhoneNumber, String otherPhoneNumber, String userActive){
        refresh.setRefreshing(true);
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_CHANGE_FRIEND_RELATIONSHIP)
                .addParams("ownPhoneNumber", ownPhoneNumber)
                .addParams("otherPhoneNumber", otherPhoneNumber)
                .addParams("userActive", userActive)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        String a = "";
                        refresh.setRefreshing(false);
                        Toast.makeText(FriendListActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(FriendListActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }
                });
    }
}
