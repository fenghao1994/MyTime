package com.example.mytime.mvp.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.util.HttpUrl;
import com.example.mytime.util.MyUtil;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

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
    @BindView(R.id.mine_open_layout)
    LinearLayout mineOpenLayout;

    private User user;
    private ArrayList<ImageItem> mImageItem;
    private SharedPreferences sp;

    private List<RelativeLayout> layouts;
    private TranslateAnimation[] list;
    float x, y;
    int[][] location;
    public static final float RADIUS = 450f;
    Handler handler = new Handler();
    Runnable[] runnables;

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

        if ( !TextUtils.isEmpty(user.getHeadImg())){
            if (user.getHeadImg().contains("D:\\")) {
                String s = user.getHeadImg();
                s = s.substring(3, s.length());
                s = s.replace("\\", "/");
                Glide.with(this).load(HttpUrl.ROOT + "/" + s).diskCacheStrategy(DiskCacheStrategy.ALL).into(headerImg);
            } else {
                Glide.with(this).load(user.getHeadImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(headerImg);
            }
        }


        initData();
       /* if (user.getLabel() != null && user.getLabel().size() > 0){
            location = new int[user.getLabel().size()][2];
            runnables = new Runnable[user.getLabel().size()];
            addLabelLayout();
        }*/
    }

    @OnClick(R.id.mine_open_layout)
    public void goMyOpen(){
        Intent intent = new Intent(this, MyOpenPlanItemActivity.class);
        startActivity(intent);
    }

    public void addLabelLayout() {
        layouts = new ArrayList<>();
        for (int i = 0; i < user.getLabel().size(); i++) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            frameLayout.addView(relativeLayout);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            layoutParams.height = MyUtil.dip2px(this, 80);
            layoutParams.width = MyUtil.dip2px(this, 80);
            relativeLayout.setLayoutParams(layoutParams);
            relativeLayout.setGravity(Gravity.CENTER);

            TextView textView = new TextView(this);
            textView.setText(user.getLabel().get(i));
            textView.setGravity(Gravity.CENTER);

            relativeLayout.addView(textView);

            layouts.add(relativeLayout);
        }

        list = new TranslateAnimation[layouts.size()];
        gameRotate();
    }

    public void gameRotate() {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rorate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            labelImg.startAnimation(operatingAnim);
            labelImg.post(new Runnable() {
                @Override
                public void run() {
                    x = labelImg.getTranslationX();
                    y = labelImg.getTranslationY();
                    other();
                }
            });
        }
    }

    public void other() {

        for (int i = 0; i < layouts.size(); i++) {
            layouts.get(i).getLocationInWindow(location[i]);
            ObjectAnimator.ofFloat(layouts.get(i), "translationX", x, (float) (x + Math.sin(Math.toRadians(i * 60)) * RADIUS)).setDuration(500).start();
            ObjectAnimator.ofFloat(layouts.get(i), "translationY", y, (float) (y - Math.cos(Math.toRadians(i * 60)) * RADIUS)).setDuration(500).start();
            location[i][0] = location[i][0] + (int) (Math.sin(Math.toRadians(i * 60)) * RADIUS);
            location[i][1] = location[i][1] - (int) (Math.cos(Math.toRadians(i * 60)) * RADIUS);
        }
        active();
    }

    public void active() {

        for (int i = 0; i < runnables.length; i++) {
            final int finalI = i;
            runnables[i] = new Runnable() {
                @Override
                public void run() {
                    float flag;
                    float flag1;
                    int[] loc = new int[2];
                    layouts.get(finalI).getLocationInWindow(loc);
                    for (int j = 0; ; j++) {
                        flag = -30f + (float) (Math.random() * 60);
                        flag1 = -30f + (float) (Math.random() * 60);
                        if (((location[finalI][0] + 30) > (loc[0] + flag) && (location[finalI][0] - 30) < (loc[0] + flag))
                                && ((location[finalI][1] + 30) > (loc[1] + flag1) && (location[finalI][1] - 30) < (loc[1] + flag1))) {
                            break;
                        }
                    }
                    ObjectAnimator.ofFloat(layouts.get(finalI), "translationX", layouts.get(finalI).getTranslationX(), layouts.get(finalI).getTranslationX() + flag).setDuration(1000).start();
                    ObjectAnimator.ofFloat(layouts.get(finalI), "translationY", layouts.get(finalI).getTranslationY(), layouts.get(finalI).getTranslationY() + flag1).setDuration(1000).start();
                    handler.postDelayed(this, 100 * (finalI + 6));
                }
            };
        }
        for (int i = 0; i < runnables.length; i++) {
            handler.postDelayed(runnables[i], 1000);
        }

    }

    public void initData() {
        if (!TextUtils.isEmpty(user.getUserName())) {
            name.setText(user.getUserName());
        }
        if (!TextUtils.isEmpty(user.getSelfIntroduction())) {
            selfIntroductionText.setText(user.getSelfIntroduction());
        }
        if (!TextUtils.isEmpty(user.getPersonalizedSignature())) {
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

    @OnClick(R.id.label_img)
    public void goMyLabel() {
        Intent intent = new Intent(this, MyLabelActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mine_friend)
    public void goMyFriend() {
        Intent intent = new Intent(this, FriendListActivity.class);
        startActivity(intent);
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
                updateUserInfo(user.getHeadImg());
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

    public void updateUserInfo(String path) {
        File file = new File(path);
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_HEADER_IMG)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addFile("file", "file1", file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Map<String, String> map = gson.fromJson(response, Map.class);
                        Toast.makeText(MineActivity.this, map.get("msg"), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
