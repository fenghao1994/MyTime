package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.application.MyApplication;
import com.example.mytime.mvp.ui.adapter.ImageItemAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class CreatePlanItemActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER = 1;
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
    @BindView(R.id.activity_create_plan_item)
    LinearLayout activityCreatePlanItem;
    @BindView(R.id.gridview)
    GridView gridview;

    ArrayList<ImageItem> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }

    @OnClick({R.id.toolbar_title, R.id.ok, R.id.toolbar, R.id.content_title, R.id.content, R.id.call_phone, R.id.send_message, R.id.photo, R.id.location})
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
                takePhoto();
                break;
            case R.id.location:
                goLocation();
                break;
        }
    }

    public void goLocation(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity( intent);
    }

    
    //// TODO: 2017/1/3  暂时不做 第三方插件有点问题
//    @OnItemClick(R.id.gridview)
//    public void onItemClick(int position){
//        Intent intent = new Intent(this, ImageZoomActivity.class);
//        intent.putExtra("image_path", images.get( position).path);
//        startActivity( intent);
//    }

    private void takePhoto(){
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS){
            if (data != null && requestCode == IMAGE_PICKER){
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItemAdapter imageItemAdapter = new ImageItemAdapter(images, this);
                gridview.setAdapter( imageItemAdapter);
            }else {
                Toast.makeText(this, "meiyou fanhui shuju", Toast.LENGTH_SHORT).show();
            }
        }
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
