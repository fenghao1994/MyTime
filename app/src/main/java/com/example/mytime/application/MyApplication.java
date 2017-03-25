package com.example.mytime.application;

import android.app.Application;

import com.example.mytime.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.mob.mobapi.MobAPI;

import org.litepal.LitePal;

import cn.smssdk.SMSSDK;

/**
 * Created by fenghao on 2017/1/3.
 */

public class MyApplication extends Application {

    ImagePicker imagePicker;
    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);

        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        MobAPI.initSDK(this, "1b7fe4470b808");

        SMSSDK.initSDK(this, "1c65bcce52490", "1b30899dd5a451437bf17a4b9275fd20");
    }
}
