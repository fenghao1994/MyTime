package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.example.mytime.R;
import com.example.mytime.mvp.ui.custom.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageZoomActivity extends AppCompatActivity {


    String path;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_image_zoom)
    CoordinatorLayout activityImageZoom;
    @BindView(R.id.photo_view)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_zoom);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        path = intent.getStringExtra("image_path");

        Glide.with(this).load(path).into(photoView);

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
