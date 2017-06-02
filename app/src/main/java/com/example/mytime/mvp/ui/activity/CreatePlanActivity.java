package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mytime.R;
import com.example.mytime.mvp.model.entity.Plan;
import com.example.mytime.mvp.model.entity.PlanItem;
import com.example.mytime.mvp.presenter.ICreatePlanPresenter;
import com.example.mytime.mvp.presenter.impl.CreatePlanPresenterImpl;
import com.example.mytime.mvp.ui.adapter.PlanItemAdapter;
import com.example.mytime.mvp.ui.custom.SharePopWindow;
import com.example.mytime.mvp.ui.custom.TitleDialog;
import com.example.mytime.mvp.ui.view.ICreatePlanView;
import com.example.mytime.util.Extra;
import com.example.mytime.util.HttpUrl;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class CreatePlanActivity extends AppCompatActivity implements ICreatePlanView, PlanItemAdapter.ShareLongClick {

    public static final int REQUEST = 1;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ok)
    ImageView ok;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_create_plan)
    CoordinatorLayout activityCreatePlan;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    Plan plan;
    long planCreateTime;
    long planEditTime;
    long planPlanId;
    String planTitle;
    boolean planIsExpired;
    boolean planIsComplete;
    boolean planIsEdit;

    boolean isCompletePlanItem;
    PlanItemAdapter adapter;

    private boolean isFromFab;
    private SharedPreferences sp;

    private ICreatePlanPresenter createPlanPresenter;

    private SharePopWindow sharePopWindow;

    private Tencent mTencent;
    //微信分享
    private static final String APP_ID = "wx6c7a491a4edb052f";
    private IWXAPI iwxapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        createPlanPresenter = new CreatePlanPresenterImpl(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        plan = (Plan) getIntent().getSerializableExtra("PLAN");
        isFromFab = getIntent().getBooleanExtra("IS_FROM_FAB", false);

        if (plan != null) {
            createPlanPresenter.showData(plan);
        } else {
            createPlanInData();
        }
        sp = getSharedPreferences("MYTIME", Context.MODE_PRIVATE);

        sharePopWindow = new SharePopWindow(this);
        mTencent = Tencent.createInstance("1106152550", this.getApplicationContext());
        reqToWx();
    }
    //注册到微信
    private void reqToWx(){
        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        iwxapi.registerApp(APP_ID);
    }
    @Override
    public void showData(List<PlanItem> planItems) {
        toolbarTitle.setText("编辑");

        if (planItems != null && planItems.size() > 0) {
            adapter = new PlanItemAdapter(this, planItems);
            adapter.setShareLongClick(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (plan != null) {
            createPlanPresenter.showData(plan);
        }
    }

    @OnClick({R.id.toolbar_title, R.id.ok, R.id.toolbar, R.id.recycler_view, R.id.activity_create_plan, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                break;
            case R.id.ok:
                clickOk();
                break;
            case R.id.toolbar:
                break;
            case R.id.recycler_view:
                break;
            case R.id.activity_create_plan:
                break;
            case R.id.fab:
                createPlanItem();
                break;
        }
    }


    private void createPlanItem() {
        Intent intent = new Intent(this, CreatePlanItemActivity.class);
        intent.putExtra("PLAN", plan);
        isCompletePlanItem = false;
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST) {
                isCompletePlanItem = data.getBooleanExtra("isCompletePlanItem", false);
            }
        }
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

    public void clickOk() {

        final TitleDialog titleDialog = new TitleDialog(this, plan.getTitle());
        titleDialog.show();

        Window window = titleDialog.getWindow();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
//        layoutParams.height = (int)(display.getHeight() * 0.2);
        layoutParams.width = (int) (display.getWidth() * 0.9);
        window.setAttributes(layoutParams);

        titleDialog.setResultListener(new TitleDialog.ResultListener() {
            @Override
            public void onResultListener(String str) {
                if (!TextUtils.isEmpty(str)) {
                    if (isCompletePlanItem && plan != null) {
                        if (isFromFab) {
                            plan.setTitle(str);
                            createPlanPresenter.savePlan(plan);
                            //等于2 上传到服务器
                            if (Extra.NET_WORK == 2){
                                saveObjectWithNetWork();
                            }

                        } else {
                            plan.setTitle(str);
                            editPlanData();
                            createPlanPresenter.updatePlan(plan);
                            //等于2 上传到服务器
                            if (Extra.NET_WORK == 2){
                                updateObjectWithNetWork();
                            }
                        }
                        titleDialog.dismiss();
                        Toast.makeText(CreatePlanActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (plan != null && plan.getTitle() != null) {
                            if (!plan.getTitle().equals(str)) {
                                plan.setTitle(str);
                                editPlanData();
                                if (isFromFab){
                                    createPlanPresenter.savePlan(plan);
                                }else {
                                    createPlanPresenter.updatePlan(plan);
                                }


                                //等于2 上传到服务器
                                if (Extra.NET_WORK == 2){
                                    updateObjectWithNetWork();
                                }
                            }
                            Toast.makeText(CreatePlanActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                            titleDialog.dismiss();
                            return;
                        }
                        Toast.makeText(CreatePlanActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                        titleDialog.dismiss();
                        return;
                    }
                } else {
                    Toast.makeText(CreatePlanActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void complete() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 创建一个新的plan对象
     */
    public void createPlanInData() {
        planCreateTime = System.currentTimeMillis();
        planEditTime = planCreateTime;
        planPlanId = planCreateTime;
        planTitle = "";
        planIsExpired = false;
        planIsComplete = false;
        planIsEdit = false;

        plan = new Plan();

        plan.setPlanId(planPlanId);
        plan.setCreateTime(planCreateTime);
        plan.setEdit(planIsEdit);
        plan.setExpired(planIsExpired);
        plan.setEditTime(planEditTime);
        plan.setComplete(planIsComplete);
        plan.setTitle(planTitle);
    }

    /**
     * 编辑plan对象
     *
     * @return
     */
    public void editPlanData() {
        planEditTime = System.currentTimeMillis();
//        planTitle = "";
        planIsEdit = true;

        plan.setEdit(planIsEdit);
        plan.setEditTime(planEditTime);
//        plan.setTitle(planTitle);
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
        super.onBackPressed();
        if (isCompletePlanItem && plan != null && isFromFab) {
            plan.setTitle("");
            createPlanPresenter.savePlan(plan);
            //等于2 上传到服务器
            if (Extra.NET_WORK == 2){
                saveObjectWithNetWork();
            }
        }
        this.finish();
    }

    public void saveObjectWithNetWork(){
        postUrl(HttpUrl.POST_SAVE_PLAN);
    }

    public void updateObjectWithNetWork(){
        postUrl(HttpUrl.POST_UPDATA_PLAN);
    }

    public void postUrl(String url){
        OkHttpUtils
                .post()
                .url(url)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("id", plan.getId() + "")
                .addParams("planId", plan.getPlanId() + "")
                .addParams("title", plan.getTitle())
                .addParams("isExpired", plan.isExpired() + "")
                .addParams("isComplete", plan.isComplete() + "")
                .addParams("createTime", plan.getCreateTime() + "")
                .addParams("editTime", plan.getEditTime() + "")
                .addParams("isEdit", plan.isEdit() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("MYTIME_OKHTTP", "更新或者保存成功");
                    }
                });
    }

    public void deletePlanItem(PlanItem planItem){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_DELETE_PLAN_ITEM)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("id", planItem.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("MYTIME_OKHTTP", "删除成功");
                    }
                });
    }

    public void completePlanItem(PlanItem planItem){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_COMPLETE_PLAN_ITEM)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("createTime", planItem.getCreateTime() + "")
                .addParams("editTime", planItem.getEditTime() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i("MYTIME_OKHTTP", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("MYTIME_OKHTTP", "删除成功");
                    }
                });
    }



    @Override
    public void shareLongClick(final PlanItem planItem) {
        sharePopWindow.showAtLocation(this.findViewById(R.id.activity_create_plan),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        sharePopWindow.setQqShareOnClick(new SharePopWindow.QQShareOnClick() {
            @Override
            public void qqShareClick() {

                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, planItem.getTitle());
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  planItem.getContent());
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  HttpUrl.GET_SHARE_PLANITEM + "?id=" + planItem.getId() + "&phoneNumber=" + sp.getString("phoneNumber", ""));
//                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "MyTime");
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                mTencent.shareToQQ(CreatePlanActivity.this, params, new BaseUiListener());
            }
        });
        sharePopWindow.setPyShareOnClick(new SharePopWindow.PYShareOnClick() {
            @Override
            public void pyShareClick() {
                choosePYOrSe(2, planItem);
            }
        });
        sharePopWindow.setWxShareOnClick(new SharePopWindow.WXShareOnClick() {
            @Override
            public void wxShareClick() {
                choosePYOrSe(1, planItem);
            }
        });
    }

    public void choosePYOrSe(int flag, PlanItem planItem){
        //flag 1为 分享到聊天界面中  2为  朋友圈
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = HttpUrl.GET_SHARE_PLANITEM + "?id=" + planItem.getId() + "&phoneNumber=" + sp.getString("phoneNumber", "");
        WXMediaMessage msg = new WXMediaMessage(wxWebpageObject);
        msg.title = planItem.getTitle();
        msg.description = planItem.getContent();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = planItem.getPlanId() + "";
        req.message = msg;
        if (flag == 1){
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        iwxapi.sendReq(req);
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
//            mBaseMessageText.setText("onComplete:");
            doComplete((JSONObject) response);
        }
        protected void doComplete(JSONObject values) {
            Toast.makeText(CreatePlanActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onError(UiError e) {
//            ("onError:", "code:" + e.errorCode + ", msg:"
//                    + e.errorMessage + ", detail:" + e.errorDetail);
            Toast.makeText(CreatePlanActivity.this, "分享发生错误", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
//            showResult("onCancel", "");
        }
    }
}
