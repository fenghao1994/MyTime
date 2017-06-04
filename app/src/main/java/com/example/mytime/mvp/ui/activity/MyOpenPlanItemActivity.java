package com.example.mytime.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MyOpenPlanItemActivity extends AppCompatActivity implements PlanItemAdapter.ShareLongClick {

    public static final int REQUEST = 1;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    boolean isCompletePlanItem;
    PlanItemAdapter adapter;

    private SharedPreferences sp;


    private SharePopWindow sharePopWindow;

    private Tencent mTencent;
    //微信分享
    private static final String APP_ID = "wx6c7a491a4edb052f";
    private IWXAPI iwxapi;

    private List<PlanItem> planItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_open_plan_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
    protected void onResume() {
        super.onResume();
        showData();
    }
    public void showData() {
        planItems = DataSupport.where("open = ?", "OPEN").find(PlanItem.class);
        if (planItems != null && planItems.size() > 0) {
            adapter = new PlanItemAdapter(this, planItems);
            adapter.setShareLongClick(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @OnClick({R.id.toolbar_title, R.id.toolbar, R.id.recycler_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_title:
                break;
            case R.id.toolbar:
                break;
            case R.id.recycler_view:
                break;
        }
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
        this.finish();
    }

    public void deletePlanItem(PlanItem planItem){
        OkHttpUtils
                .post()
                .url(HttpUrl.POST_DELETE_PLAN_ITEM)
                .addParams("phoneNumber", sp.getString("phoneNumber", ""))
                .addParams("createTime", planItem.getCreateTime() + "")
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
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  HttpUrl.GET_SHARE_PLANITEM + "?createTime=" + planItem.getCreateTime() + "&phoneNumber=" + sp.getString("phoneNumber", ""));
//                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "MyTime");
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
                mTencent.shareToQQ(MyOpenPlanItemActivity.this, params, new MyOpenPlanItemActivity.BaseUiListener());
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
        wxWebpageObject.webpageUrl = HttpUrl.GET_SHARE_PLANITEM + "?createTime=" + planItem.getCreateTime() + "&phoneNumber=" + sp.getString("phoneNumber", "");
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
            Toast.makeText(MyOpenPlanItemActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onError(UiError e) {
//            ("onError:", "code:" + e.errorCode + ", msg:"
//                    + e.errorMessage + ", detail:" + e.errorDetail);
            Toast.makeText(MyOpenPlanItemActivity.this, "分享发生错误", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel() {
//            showResult("onCancel", "");
        }
    }
}
