package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.ui.adapter.MyLabelAdapter;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置自己的标签
 */
public class MyLabelActivity extends AppCompatActivity implements MyLabelAdapter.OnDeleteLabel {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.label)
    EditText label;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_my_label)
    LinearLayout activityMyLabel;

    private User user;
    private MyLabelAdapter labelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_label);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = DataSupport.findFirst(User.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        if (user.getLabel() != null){
            labelAdapter = new MyLabelAdapter(this, user.getLabel());
            recyclerView.setAdapter(labelAdapter);
            labelAdapter.setOnDeleteLabel(this);
        }
    }


    @OnClick(R.id.ok)
    public void okClick(){
        final String str = label.getText().toString().trim();
        if (TextUtils.isEmpty(str) || str.length() > 4){
            Toast.makeText(this, "标签不能为空和不能超过四个字", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.getLabel() != null && user.getLabel().size() >= 6){
            Toast.makeText(this, "标签不能超过6个", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_SAVE_USER_LABEL)
                .addParams("phoneNumber", user.getPhoneNumber())
                .addParams("label", str)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MyLabelActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        label.setText("");
                        Toast.makeText(MyLabelActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        if (user.getLabel() == null){
                            List<String> list = new ArrayList<>();
                            list.add(str);
                            user.setLabel(list);
                        }else {
                            user.getLabel().add(str);
                        }
                        user.saveOrUpdate();
                        if (labelAdapter != null){
                            labelAdapter.notifyDataSetChanged();
                        }else {
                            labelAdapter = new MyLabelAdapter(MyLabelActivity.this, user.getLabel());
                            recyclerView.setAdapter(labelAdapter);
                            labelAdapter.setOnDeleteLabel(MyLabelActivity.this);
                        }
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
    public void onDeleteLabel(final String str) {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_DELETE_USER_LABEL)
                .addParams("phoneNumber", user.getPhoneNumber())
                .addParams("label", str)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MyLabelActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MyLabelActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        user.getLabel().remove(str);
                        user.saveOrUpdate();
                        if (labelAdapter != null){
                            labelAdapter.notifyDataSetChanged();
                        }else {
                            labelAdapter = new MyLabelAdapter(MyLabelActivity.this, user.getLabel());
                            recyclerView.setAdapter(labelAdapter);
                            labelAdapter.setOnDeleteLabel(MyLabelActivity.this);
                        }
                    }
                });
    }
}
