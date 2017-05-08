package com.example.mytime.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.mytime.event.LocalEvent;
import com.example.mytime.event.PlanItemRefreshEvent;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.receiver.AlarmReceiver;
import com.example.mytime.util.Extra;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fenghao02 on 2017/3/23.
 */

public class LocalService extends Service {

    private LocalEvent localEvent;

    private AMapLocationClient mlocationClient;

    private List<PlanItem> mPlanItem;
    ;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

    @Override
    public void onCreate() {
        super.onCreate();
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(locationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        //设置定位参数
        mlocationClient.setLocationOption( getDefaultOption());
        //启动定位
        mlocationClient.startLocation();

        EventBus.getDefault().register( this);

        getPlanItemsList( null);

    }

    @Subscribe
    public void getPlanItemsList(PlanItemRefreshEvent refreshEvent){
        mPlanItem = DataSupport.where("isComplete = ? and location != ?", "false", "null").find(PlanItem.class);
    }



    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(30000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    amapLocation.getProvince();//身份信息
                    amapLocation.getCity(); //city信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间



                    localEvent = new LocalEvent(amapLocation.getProvince(), amapLocation.getCity());
                    EventBus.getDefault().post( localEvent);
//                    stopLocation();
//                    destroyLocation();
                    getDistance(amapLocation.getLatitude(), amapLocation.getLongitude());
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            } else {
//                tvResult.setText("定位失败，loc is null");
            }
        }
    };

    private void getDistance(double endLatitude, double endLongitude){
//        getPlanItemsList( null);
        getPlanItem();
        for (int i = 0 ; i < mPlanItem.size(); i++){
            String distance = mPlanItem.get(i).getLocation();
            String[] locations = distance.split(",");
            double startLatitude = Double.parseDouble(locations[0]);
            double startLongitude = Double.parseDouble(locations[1]);

            float[] result = new float[1];
            Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, result);
            if ( result[0] >= 0 && result[0] < 50){
                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.setAction(Extra.ALARM_LOCATION);
                intent.putExtra("PLAN_ITEM", mPlanItem.get( i));
                sendBroadcast( intent);
            }
            Log.i("LocalService", "distance is jin ");
        }
    }

    public void getPlanItem(){
        mPlanItem = new ArrayList<>();
        List<PlanItem> list = DataSupport.findAll(PlanItem.class);
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).isComplete() == false && list.get(i).getLocation() != null && list.get(i).getDescribe() == null){
                mPlanItem.add( list.get(i));
                list.get(i).setDescribe("LOCATED");
                list.get(i).save();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        mlocationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister( this);
    }
}
