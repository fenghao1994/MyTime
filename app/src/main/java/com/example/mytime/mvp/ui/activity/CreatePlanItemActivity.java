package com.example.mytime.mvp.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.presenter.ICreatePlanItemPresenter;
import com.example.mytime.mvp.presenter.impl.CreatePlanItemPresenterImpl;
import com.example.mytime.mvp.ui.receiver.AlarmReceiver;
import com.example.mytime.mvp.ui.adapter.EasyGridviewAdapter;
import com.example.mytime.mvp.ui.adapter.ImageItemAdapter;
import com.example.mytime.mvp.ui.custom.DateDialog;
import com.example.mytime.mvp.ui.custom.TimeDialog;
import com.example.mytime.mvp.ui.view.ICreatePlanItemView;
import com.example.mytime.util.Extra;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class CreatePlanItemActivity extends AppCompatActivity implements ICreatePlanItemView {

    private static final int IMAGE_PICKER = 1;
    private static final int REQUEST_LOCATION = 2;
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

    DateDialog dateDialog;
    TimeDialog timeDialog;

    Plan plan;
    PlanItem planItem;

    String planItemTitle;
    String planItemContentMessage;
    long planItemCreateTime;
    long planItemEditTime;
    boolean planItemIsEdit;
    String planItemPhoneNumber;
    String planItemMessageContent;
    String planItemMessagePhoneNumber;
    String planItemLocation;
    boolean planItemIsEveryDay;
    boolean planItemIsManyDays;
    boolean planItemIsExpired;
    boolean planItemIsComplete;
    ArrayList<Photo> planItemAddress;

    //设置一个-1来判断是否真的点击设定按钮
    int planItemYear = -1, planItemMonth, planItemDay, planItemHour = -1, planItemMinute, planItemAlarmWay;
    /**
     * 临时对象
     * 为了获取当前planitem对象的id
     */
    PlanItem currentPlanItem;

    EasyGridviewAdapter easyGridviewAdapter;

    ICreatePlanItemPresenter createPlanItemPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        createPlanItemPresenter = new CreatePlanItemPresenterImpl(this);


        dateDialog = new DateDialog(this);
        timeDialog = new TimeDialog(this);

        Intent intent = getIntent();
        plan = (Plan) intent.getSerializableExtra("PLAN");

        planItem = (PlanItem) intent.getSerializableExtra("PLANITEM");
        if (planItem != null) {
            createPlanItemPresenter.showData(planItem);
        }
    }

    @Override
    public void showData(PlanItem planItem, List<Photo> photos) {
        toolbarTitle.setText("编辑");
        contentTitle.setText(planItem.getTitle());
        content.setText(planItem.getContent());
        planItemAddress = (ArrayList<Photo>) photos;
        easyGridviewAdapter = new EasyGridviewAdapter(this, planItemAddress);
        gridview.setAdapter(easyGridviewAdapter);
        planItemLocation = planItem.getLocation();
        planItemYear = planItem.getYear();
        planItemMonth = planItem.getMonth();
        planItemDay = planItem.getDay();
        planItemHour = planItem.getHour();
        planItemMinute = planItem.getMinute();
        planItemAlarmWay = planItem.getAlarmWay();
        planItemPhoneNumber = planItem.getPhoneNumber();
        planItemMessageContent = planItem.getMessageContent();
        planItemMessagePhoneNumber = planItem.getMessagePhoneNumber();
        planItemIsEveryDay = planItem.isEveryDay();
        planItemIsManyDays = planItem.isManyDays();
        planItemIsExpired = planItem.isExpired();
        planItemIsComplete = planItem.isComplete();
    }


    @OnClick({R.id.time, R.id.toolbar_title, R.id.ok, R.id.toolbar, R.id.content_title, R.id.content, R.id.call_phone, R.id.send_message, R.id.photo, R.id.location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                break;
            case R.id.ok:
                submit();
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
            case R.id.time:
                showDateDialog();
                break;
        }
    }

    /**
     * 提交信息
     */
    public void submit() {
        planItemTitle = contentTitle.getText().toString();
        planItemContentMessage = content.getText().toString();
        if (TextUtils.isEmpty(planItemTitle) && TextUtils.isEmpty(planItemContentMessage)) {
            Toast.makeText(this, "请输入标题或者内容", Toast.LENGTH_SHORT).show();
            return;
        }
        if (planItemYear == -1 || planItemHour == -1){
            Toast.makeText(this, "请选择提醒时间", Toast.LENGTH_SHORT).show();
            return;
        }

        if (planItem == null) {
            planItemCreateTime = System.currentTimeMillis();
            planItemEditTime = System.currentTimeMillis();
            if ( planItemAlarmWay != 0){
                planItemIsManyDays = true;
            }
            planItem = new PlanItem();
            planItem.setPlanId(plan.getPlanId());
            planItem.setTitle(planItemTitle);
            planItem.setContent(planItemContentMessage);
            planItem.setCreateTime(planItemCreateTime);
            planItem.setEditTime(planItemEditTime);
            planItem.setEdit(planItemIsEdit);
            planItem.setPhoneNumber(planItemPhoneNumber);
            planItem.setMessageContent(planItemMessageContent);
            planItem.setMessagePhoneNumber(planItemMessagePhoneNumber);
            planItem.setLocation(planItemLocation);
            planItem.setYear( planItemYear);
            planItem.setMonth( planItemMonth);
            planItem.setDay( planItemDay);
            planItem.setHour( planItemHour);
            planItem.setMinute( planItemMinute);
            planItem.setAlarmWay( planItemAlarmWay);
            planItem.setAddress(planItemAddress);
            planItem.setEveryDay(planItemIsEveryDay);
            planItem.setManyDays(planItemIsManyDays);
            planItem.setExpired(planItemIsExpired);
            planItem.setComplete(planItemIsComplete);

            createPlanItemPresenter.savePlanItem(planItem, planItemAddress);
        }
        else {
            planItemEditTime = System.currentTimeMillis();
            planItemIsEdit = true;
            planItem.setTitle(planItemTitle);
            planItem.setContent(planItemContentMessage);
            planItem.setEditTime(planItemEditTime);
            planItem.setEdit(planItemIsEdit);
            planItem.setPhoneNumber(planItemPhoneNumber);
            planItem.setMessageContent(planItemMessageContent);
            planItem.setMessagePhoneNumber(planItemMessagePhoneNumber);
            planItem.setLocation(planItemLocation);
            planItem.setYear( planItemYear);
            planItem.setMonth( planItemMonth);
            planItem.setDay( planItemDay);
            planItem.setHour( planItemHour);
            planItem.setMinute( planItemMinute);
            planItem.setAlarmWay( planItemAlarmWay);
            planItem.setAddress(planItemAddress);
            planItem.setEveryDay(planItemIsEveryDay);
            planItem.setManyDays(planItemIsManyDays);
            planItem.setExpired(planItemIsExpired);
            planItem.setComplete(planItemIsComplete);
            createPlanItemPresenter.updatePlanItem( planItem, planItemAddress);
        }
    }

    /**
     * 完成设置退出页面
     */
    @Override
    public void complete() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

    //// TODO: 2017/1/5 设置闹钟
    @Override
    public void setAlarm(Calendar calendar) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, planItem.getId(), intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //AlarmManager.RTC_WAKEUP，硬件闹钟，当闹钟发射时唤醒手机休眠；

        if (planItem.getAlarmWay() == 0){
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }else if (planItem.getAlarmWay() == 1){
            //每天
            long time = 1000 * 60 * 60 * 24;
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),time, pi);
        }else if (planItem.getAlarmWay() == 2){
            long time = 1000 * 60 * 60 * 24 * 7;
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),time, pi);
        }/*else if (planItem.getAlarmWay() == 3){

        }*/
    }

    /**
     * 展示dialog
     */
    public void showDateDialog() {
        dateDialog.show();
        dateDialog.setOnOkListener(new DateDialog.onOkListener() {
            @Override
            public void getDate(int year, int month, int day) {
                CreatePlanItemActivity.this.planItemYear = year;
                CreatePlanItemActivity.this.planItemMonth = month;
                CreatePlanItemActivity.this.planItemDay = day;
                dateDialog.dismiss();

                timeDialog.show();
                timeDialog.setOnOkListener(new TimeDialog.onOkListener() {
                    @Override
                    public void getTime(int hour, int min, int alarmWay) {
                        CreatePlanItemActivity.this.planItemHour = hour;
                        CreatePlanItemActivity.this.planItemMinute = min;
                        CreatePlanItemActivity.this.planItemAlarmWay = alarmWay;
                        timeDialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 获取地理位置
     */
    public void goLocation() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, REQUEST_LOCATION);
    }


    @OnItemClick(R.id.gridview)
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ImageZoomActivity.class);
        intent.putExtra("image_path", planItemAddress.get(position).getAddress());
        startActivity(intent);
    }

    private void takePhoto() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItemAdapter imageItemAdapter = new ImageItemAdapter(images, this);
                gridview.setAdapter(imageItemAdapter);
                planItemAddress = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    Photo photo = new Photo();
                    photo.setAddress(images.get(i).path);
                    planItemAddress.add(photo);
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == MapActivity.RESULT_LOCATION) {
            if (data != null && requestCode == REQUEST_LOCATION) {
                planItemLocation = data.getStringExtra("LOCATION");
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

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
