package com.example.mytime.mvp.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoftWareInfoActivity extends AppCompatActivity {

    @BindView(R.id.version_name)
    TextView versionName;
    @BindView(R.id.software_info)
    TextView softwareInfo;
    @BindView(R.id.activity_soft_ware_info)
    LinearLayout activitySoftWareInfo;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_ware_info);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        }

        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        String appVersion = "";
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName.setText("版本: v" + appVersion + " (已是最新版)");
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
}
