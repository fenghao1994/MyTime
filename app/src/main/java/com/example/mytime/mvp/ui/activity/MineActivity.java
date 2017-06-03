package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.Extra;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineActivity extends AppCompatActivity {

    @BindView(R.id.header_img)
    CircleImageView headerImg;
    @BindView(R.id.label_img)
    ImageView labelImg;
    @BindView(R.id.mine_friend)
    ImageView mineFriend;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name_layout)
    LinearLayout nameLayout;
    @BindView(R.id.personalized_signature_layout)
    LinearLayout personalizedSignatureLayout;
    @BindView(R.id.self_introduction_layout)
    LinearLayout selfIntroductionLayout;
    @BindView(R.id.mine_phoneNumber)
    TextView minePhoneNumber;
    @BindView(R.id.activity_mine)
    LinearLayout activityMine;
    @BindView(R.id.personalized_text)
    TextView personalizedText;
    @BindView(R.id.self_introduction_text)
    TextView selfIntroductionText;

    private User user;
    private ArrayList<ImageItem> mImageItem;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = DataSupport.findFirst(User.class);
        minePhoneNumber.setText(user.getPhoneNumber());
        initData();
    }

    public void initData() {
        if (!TextUtils.isEmpty(user.getUserName())) {
            name.setText(user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getSelfIntroduction())) {
            selfIntroductionText.setText(user.getSelfIntroduction());
        }
        if (!TextUtils.isEmpty(user.getPersonalizedSignature())){
            personalizedText.setText(user.getPersonalizedSignature());
        }
    }

    @OnClick(R.id.header_img)
    public void headImgClick() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 1) {
                mImageItem = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Glide.with(this).load(mImageItem.get(0).path).into(headerImg);
                sp.edit().putString("path", mImageItem.get(0).path).commit();
                user.setHeadImg(mImageItem.get(0).path);
                user.saveOrUpdate();
                //如果等于2  需要上传到服务器
                if (Extra.NET_WORK == 2) {
//                    submitHeadImg(mImageItem.get(0).path);
                    updateUserInfo();
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.back)
    public void backClick() {
        this.finish();
    }

    @OnClick(R.id.name_layout)
    public void changeName() {
        Intent intent = new Intent(this, ChangeMessageActivity.class);
        intent.putExtra("FLAG", "NAME");
        startActivity(intent);
    }

    @OnClick(R.id.personalized_signature_layout)
    public void changePersonalizedSignature() {
        Intent intent = new Intent(this, ChangeMessageActivity.class);
        intent.putExtra("FLAG", "SIGNATURE");
        startActivity(intent);
    }

    @OnClick(R.id.self_introduction_layout)
    public void changeIntroduction() {
        Intent intent = new Intent(this, ChangeMessageActivity.class);
        intent.putExtra("FLAG", "INTRODUCTION");
        startActivity(intent);
    }

    public void updateUserInfo() {

    }
}
