package com.example.mytime.mvp.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Note;
import com.example.mytime.mvp.model.entity.Photo;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.model.entity.RiJi;
import com.example.mytime.mvp.model.entity.User;
import com.example.mytime.mvp.ui.activity.AllNoteActivity;
import com.example.mytime.mvp.ui.activity.AllPlanActivity;
import com.example.mytime.mvp.ui.activity.ChangeMessageActivity;
import com.example.mytime.mvp.ui.activity.CountActivity;
import com.example.mytime.mvp.ui.activity.MyFeedBackActivity;
import com.example.mytime.mvp.ui.activity.RiJiActivity;
import com.example.mytime.mvp.ui.activity.SoftWareInfoActivity;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 侧栏fragment
 */

public class NavigationFragment extends Fragment {

    @BindView(R.id.header_img)
    CircleImageView headerImg;
    @BindView(R.id.plan_layout)
    LinearLayout planLayout;
    @BindView(R.id.complete_layout)
    LinearLayout completeLayout;
    @BindView(R.id.note_layout)
    LinearLayout noteLayout;
    @BindView(R.id.count_layout)
    LinearLayout countLayout;
    @BindView(R.id.sign_out_layout)
    LinearLayout signOutLayout;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.my_riji)
    LinearLayout myRiji;
    @BindView(R.id.my_feed_back)
    LinearLayout myFeedBack;
    @BindView(R.id.software_info)
    LinearLayout softwareInfo;
    @BindView(R.id.signature)
    TextView signature;
    @BindView(R.id.signature_layout)
    LinearLayout signatureLayout;

    private ArrayList<ImageItem> mImageItem;
    private SharedPreferences sp;
    private String headerPath;
    private AlertDialog alertDialog;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, view);
        sp = getActivity().getSharedPreferences("MYTIME", Context.MODE_PRIVATE);
        if (Extra.NET_WORK == 1) {
            signOutLayout.setVisibility(View.GONE);
        } else {
            signOutLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        user = DataSupport.findFirst(User.class);
        headerPath = user.getHeadImg();
        showHeadImg();
        signature.setText(user.getPersonalizedSignature() == null? "" : user.getPersonalizedSignature() );
    }

    public void showHeadImg() {
        if (Extra.NET_WORK == 1) {
            if (!"".equals(headerPath)) {
                Glide.with(this).load(headerPath).into(headerImg);
            }
        } else {
            if (TextUtils.isEmpty(headerPath)) {
                getHeadImg();
            } else {
                if (headerPath.contains("D:\\")) {
                    String s = headerPath;
                    s = s.substring(3, s.length());
                    s = s.replace("\\", "/");
                    Glide.with(getActivity()).load(HttpUrl.ROOT + "/" + s).diskCacheStrategy(DiskCacheStrategy.ALL).into(headerImg);
                } else {
                    Glide.with(getActivity()).load(headerPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(headerImg);
                }
            }
        }

    }

    public void getHeadImg() {
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_GET_HEADIMG)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        user.setHeadImg(response);
                        user.saveOrUpdate();
                        headerPath = response;
                        headerPath = headerPath.substring(3, headerPath.length());
                        headerPath = headerPath.replace("\\", "/");
                        Glide.with(getActivity()).load(HttpUrl.ROOT + "/" + headerPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(headerImg);
                    }
                });
    }

    @OnClick(R.id.signature_layout)
    public void goChangeSignature(){
        Intent intent = new Intent(getActivity(), ChangeMessageActivity.class);
        intent.putExtra("FLAG", "SIGNATURE");
        startActivity(intent);
    }

    @OnClick({R.id.header_img, R.id.plan_layout, R.id.complete_layout, R.id.note_layout, R.id.count_layout, R.id.sign_out_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_img:
                choosePhoto();
                break;
            case R.id.plan_layout:
                showAllPlan();
                break;
            case R.id.complete_layout:
                showCompletePlan();
                break;
            case R.id.note_layout:
                showAllNote();
                break;
            case R.id.count_layout:
                showCountPlan();
                break;
            case R.id.sign_out_layout:
                signout();
                break;
        }
    }

    @OnClick(R.id.my_feed_back)
    public void goMyFeedBack() {
        Intent intent = new Intent(getActivity(), MyFeedBackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.software_info)
    public void goSoftwareInfo() {
        Intent intent = new Intent(getActivity(), SoftWareInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.my_riji)
    public void goMyRiJi() {
        Intent intent = new Intent(getActivity(), RiJiActivity.class);
        startActivity(intent);
    }

    private void signout() {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage("确定退出吗?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        DataSupport.deleteAll(Note.class);
                        DataSupport.deleteAll(Photo.class);
                        DataSupport.deleteAll(Plan.class);
                        DataSupport.deleteAll(PlanItem.class);
                        DataSupport.deleteAll(RiJi.class);
                        DataSupport.deleteAll(User.class);
                        boolean b = getActivity().deleteDatabase("mytime");
                        sp.edit().clear().commit();
                        getActivity().finish();
                        System.exit(0);
                    }
                })
                .show();
    }

    private void choosePhoto() {
        Intent intent = new Intent(getActivity(), ImageGridActivity.class);
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
                    submitHeadImg(mImageItem.get(0).path);
                }

            } else {
                Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void submitHeadImg(String path) {
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
                        Toast.makeText(getActivity(), map.get("msg"), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void showCountPlan() {
        Intent intent = new Intent(getActivity(), CountActivity.class);
        startActivity(intent);
    }

    public void showAllNote() {
        Intent intent = new Intent(getActivity(), AllNoteActivity.class);
        startActivity(intent);
    }

    public void showAllPlan() {
        Intent intent = new Intent(getActivity(), AllPlanActivity.class);
        intent.putExtra("COMPLETEPLAN", false);
        startActivity(intent);
    }

    public void showCompletePlan() {
        Intent intent = new Intent(getActivity(), AllPlanActivity.class);
        intent.putExtra("COMPLETEPLAN", true);
        startActivity(intent);
    }
}
