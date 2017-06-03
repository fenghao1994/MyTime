package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.ui.adapter.EasyGridviewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendPlanItemActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.activity_friend_plan_item)
    LinearLayout activityFriendPlanItem;


    private PlanItem planItem;
    EasyGridviewAdapter easyGridviewAdapter;
    private int mWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_plan_item);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
        planItem = (PlanItem) getIntent().getSerializableExtra("PLANITEM");
        init();
    }

    private void init(){
        title.setText(planItem.getTitle() == null ? "无标题" : planItem.getTitle());
        content.setText(planItem.getContent() == null ? "无内容" : planItem.getContent());
        time.setText(planItem.getYear() + "." + planItem.getMonth() + "." + planItem.getDay() + " " + planItem.getHour() + ":" + planItem.getMinute());

        getWidth();
        easyGridviewAdapter = new EasyGridviewAdapter(this, mWidth);
        gridview.setAdapter(easyGridviewAdapter);
        if (planItem.getAddress() != null){
            easyGridviewAdapter.setData(planItem.getAddress());
        }


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendPlanItemActivity.this, ImageZoomActivity.class);
                intent.putExtra("image_path", planItem.getAddress().get(position).getAddress());
                startActivity(intent);
            }
        });
    }
    /**
     * 获取屏幕宽度的1/3
     */
    public void getWidth(){
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        mWidth = display.getWidth() / 3;
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
}
