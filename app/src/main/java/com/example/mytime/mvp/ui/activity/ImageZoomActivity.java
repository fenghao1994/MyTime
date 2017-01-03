package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomActivity extends AppCompatActivity {


    String path;


    PhotoViewAttacher mAttacher;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photo_view)
    PhotoView photoView;
    @BindView(R.id.activity_image_zoom)
    CoordinatorLayout activityImageZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        ButterKnife.bind(this);

        setSupportActionBar( toolbar);
        Intent intent = getIntent();
        path = intent.getStringExtra("image_path");


        photoView.setImageURI(Uri.parse(path));
        mAttacher = new PhotoViewAttacher(photoView);

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
