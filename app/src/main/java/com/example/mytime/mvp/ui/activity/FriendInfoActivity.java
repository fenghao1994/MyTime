package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.HttpUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 好友信息
 */
public class FriendInfoActivity extends AppCompatActivity {

    @BindView(R.id.header_img)
    CircleImageView headerImg;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mine_phoneNumber)
    TextView minePhoneNumber;
    @BindView(R.id.personalized_text)
    TextView personalizedText;
    @BindView(R.id.self_introduction_text)
    TextView selfIntroductionText;
    @BindView(R.id.open_planitem_layout)
    LinearLayout openPlanitemLayout;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        ButterKnife.bind(this);

        user = (User) getIntent().getSerializableExtra("FRIEND");
        init();
    }

    public void init(){
        if ( user.getHeadImg().contains("D:\\")){
            String s = user.getHeadImg();
            s = s.substring(3, s.length());
            s = s.replace("\\", "/");
            Glide.with(this).load(HttpUrl.ROOT + "/" + s).diskCacheStrategy(DiskCacheStrategy.ALL).into( headerImg);
        }else {
            Glide.with(this).load(user.getHeadImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into( headerImg);
        }
        minePhoneNumber.setText(user.getPhoneNumber());

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

    @OnClick(R.id.open_planitem_layout)
    public void goOpenPlanitem(){
        Intent intent = new Intent(this, FriendOpenPlanItemActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    @OnClick(R.id.back)
    public void back(){
        finish();
    }
}
