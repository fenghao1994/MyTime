package com.example.mytime.mvp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePlanItem extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_title)
    EditText contentTitle;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.call_phone)
    ImageView callPhone;
    @BindView(R.id.send_message)
    ImageView sendMessage;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.location)
    ImageView location;
    @BindView(R.id.time)
    ImageView time;
    @BindView(R.id.photo_recycler)
    RecyclerView photoRecycler;
    @BindView(R.id.activity_create_plan_item)
    LinearLayout activityCreatePlanItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_item);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.toolbar_title, R.id.ok, R.id.toolbar, R.id.content_title, R.id.content, R.id.call_phone, R.id.send_message, R.id.photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                break;
            case R.id.ok:
                break;
            case R.id.toolbar:
                break;
            case R.id.content_title:
                break;
            case R.id.content:
                break;
            case R.id.call_phone:
                break;
            case R.id.send_message:
                break;
            case R.id.photo:
                break;
        }
    }
}
