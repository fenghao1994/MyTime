package com.example.mytime.mvp.ui.activity;

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
import com.example.mytime.mvp.model.entity.Time;
import com.example.mytime.mvp.ui.adapter.EasyGridviewAdapter;
import com.example.mytime.mvp.ui.adapter.ImageItemAdapter;
import com.example.mytime.mvp.ui.custom.TimeDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class CreatePlanItemActivity extends AppCompatActivity {

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

    TimeDialog timeDialog;
    List<Long> settingTimes = null;

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
    ArrayList<Time> planItemTimes;

    /**
     * 临时对象
     * 为了获取当前planitem对象的id
     */
    PlanItem currentPlanItem;

    EasyGridviewAdapter easyGridviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        timeDialog = new TimeDialog(this);

        Intent intent = getIntent();
        plan = (Plan) intent.getSerializableExtra("PLAN");
        planItem = (PlanItem) intent.getSerializableExtra("PLANITEM");
        if ( planItem != null){
            toolbarTitle.setText("编辑");
            contentTitle.setText( planItem.getTitle());
            content.setText( planItem.getContent());

            planItemAddress = (ArrayList<Photo>) DataSupport.where("objectType = ? and objectId = ?", "1", planItem.getId() + "").find(Photo.class);
            easyGridviewAdapter = new EasyGridviewAdapter(this, planItemAddress);
            gridview.setAdapter( easyGridviewAdapter);

            planItemTimes = (ArrayList<Time>) DataSupport.where("planItemId = ?", planItem.getId() + "").find( Time.class);

        }
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
                showTimeDialog();
                break;
        }
    }

    /**
     * 提交信息
     */
    public void submit(){
        planItemTitle = contentTitle.getText().toString();
        planItemContentMessage = content.getText().toString();
        if (TextUtils.isEmpty( planItemTitle) && TextUtils.isEmpty( planItemContentMessage)){
            Toast.makeText(this, "请输入标题或者内容", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (planItemTimes == null){
            Toast.makeText(this, "请选择提醒时间", Toast.LENGTH_SHORT).show();
            return;
        }

        if (planItem == null){
            planItemCreateTime = System.currentTimeMillis();
            planItemEditTime = System.currentTimeMillis();

            if ( planItemTimes.size() > 1){
                planItemIsManyDays = true;
            }
            planItem = new PlanItem();
            planItem.setPlanId( plan.getPlanId());
            planItem.setTitle( planItemTitle);
            planItem.setContent( planItemContentMessage);
            planItem.setCreateTime( planItemCreateTime);
            planItem.setEditTime( planItemEditTime);
            planItem.setEdit( planItemIsEdit);
            planItem.setPhoneNumber( planItemPhoneNumber);
            planItem.setMessageContent( planItemMessageContent);
            planItem.setMessagePhoneNumber( planItemMessagePhoneNumber);
            planItem.setLocation( planItemLocation);
            planItem.setTimes( planItemTimes);
            planItem.setAddress( planItemAddress);
            planItem.setEveryDay( planItemIsEveryDay);
            planItem.setManyDays( planItemIsManyDays);
            planItem.setExpired( planItemIsExpired);
            planItem.setComplete( planItemIsComplete);

            //存成功后，通过创建时间 得到当前对象，然后去存储time表和photo表
            if ( planItem.save()){
                currentPlanItem = DataSupport.where("createTime = ?", planItemCreateTime + "").findFirst( PlanItem.class);
                if (planItemTimes != null && planItemTimes.size() > 0){
                    for ( int i = 0; i < planItemTimes.size() ; i++){
                        planItemTimes.get(i).setPlanItemId( currentPlanItem.getId());
                        planItemTimes.get(i).save();
                    }
                }
                if (planItemAddress != null && planItemAddress.size() > 0){
                    for ( int i = 0; i < planItemAddress.size(); i++){
                        planItemAddress.get(i).setObjectType(1);
                        planItemAddress.get(i).setObjectId( currentPlanItem.getId());
                        planItemAddress.get(i).save();
                    }
                }
                setAlarmClock();
                complete();

            }else {
                Toast.makeText(this, "设置失败",Toast.LENGTH_SHORT).show();
            }
        }else {
            planItemEditTime = System.currentTimeMillis();
            planItemIsEdit = true;
            if ( planItemTimes.size() > 1){
                planItemIsManyDays = true;

                planItem.setTitle( planItemTitle);
                planItem.setContent( planItemContentMessage);
                planItem.setEditTime( planItemEditTime);
                planItem.setEdit( planItemIsEdit);
                planItem.setPhoneNumber( planItemPhoneNumber);
                planItem.setMessageContent( planItemMessageContent);
                planItem.setMessagePhoneNumber( planItemMessagePhoneNumber);
                planItem.setLocation( planItemLocation);
                planItem.setTimes( planItemTimes);
                planItem.setAddress( planItemAddress);
                planItem.setEveryDay( planItemIsEveryDay);
                planItem.setManyDays( planItemIsManyDays);
                planItem.setExpired( planItemIsExpired);
                planItem.setComplete( planItemIsComplete);

                planItem.update( planItem.getId());

                DataSupport.deleteAll(Time.class, "planItemId = ?", planItem.getPlanId() + "");
                if (planItemTimes != null && planItemTimes.size() > 0){
                    for ( int i = 0; i < planItemTimes.size() ; i++){
                        planItemTimes.get(i).setPlanItemId( planItem.getId());
                        planItemTimes.get(i).save();
                    }
                }

                if (planItemAddress != null ){
                    DataSupport.deleteAll(Photo.class, "objectType = ? and objectId = ?", 1 + "", planItem.getId() + "");
                    for ( int i = 0; i < planItemAddress.size(); i++){
                        planItemAddress.get(i).setObjectType(1);
                        planItemAddress.get(i).setObjectId( planItem.getId());
                        planItemAddress.get(i).save();
                    }
                }

                setAlarmClock();
                complete();
            }
        }


    }


    //// TODO: 2017/1/4 设置闹钟
    public void setAlarmClock(){

    }

    /**
     * 完成设置退出页面
     */
    public void complete(){
        Intent intent = new Intent();
        setResult( RESULT_OK, intent);
        this.finish();
    }

    /**
     * 展示dialog
     */
    public void showTimeDialog(){
        timeDialog.show();
        timeDialog.setOnOkListener(new TimeDialog.onOkListener() {
            @Override
            public void getDate(boolean isEveryDay, List<Long> setTimes) {
                planItemIsEveryDay = isEveryDay;
                planItemTimes = new ArrayList<Time>();
                for (int i = 0 ; i < setTimes.size() ;i++){
                    Time time = new Time();
                    time.setTime( setTimes.get( i));
                    planItemTimes.add( time);
                }
                timeDialog.dismiss();
            }
        });
    }

    /**
     *获取地理位置
     */
    public void goLocation(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult( intent, REQUEST_LOCATION);
    }

    
    @OnItemClick(R.id.gridview)
    public void onItemClick(int position){
        Intent intent = new Intent(this, ImageZoomActivity.class);
        intent.putExtra("image_path", planItemAddress.get( position).getAddress());
        startActivity( intent);
    }

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
                planItemAddress = new ArrayList<>();
                for (int i = 0 ; i < images.size(); i++){
                    Photo photo = new Photo();
                    photo.setAddress( images.get(i).path);
                    planItemAddress.add( photo);
                }
            }else {
                Toast.makeText(this, "meiyou fanhui shuju", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == MapActivity.RESULT_LOCATION){
            if (data != null && requestCode == REQUEST_LOCATION){
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
