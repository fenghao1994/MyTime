package com.example.mytime.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mytime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageZoomActivity extends AppCompatActivity {


    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.toolbar_layout)
    RelativeLayout toolbarLayout;
    @BindView(R.id.activity_image_zoom)
    RelativeLayout activityImageZoom;
    String uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        uri = intent.getStringExtra("image_path");
    }

    @OnClick({R.id.photo_view, R.id.img, R.id.back, R.id.toolbar_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo_view:
//                photoViewClick();
                break;
            case R.id.img:
//                imgClick();
                break;
            case R.id.back:
//                onBackPressed();
                break;
            case R.id.toolbar_layout:
                break;
        }
    }

//    private void imgClick() {
//        info = PhotoView.getImageViewInfo(img);
//        img.setVisibility(View.GONE);
//        photoView.setVisibility(View.VISIBLE);
//        photoView.setImageURI(Uri.parse(uri));
//        photoView.animaFrom(info);
//        Toast.makeText(ImageZoomActivity.this, "img", Toast.LENGTH_SHORT).show();
//    }

//    private void photoViewClick() {
//       photoView.animaTo(info, new Runnable() {
//           @Override
//           public void run() {
//               Toast.makeText(ImageZoomActivity.this, "xxx", Toast.LENGTH_SHORT).show();
//           }
//       });
//    }

//    @Override
//    public void onBackPressed() {
//        if (photoView.getVisibility() == View.VISIBLE) {
//            photoView.animaTo(info, new Runnable() {
//                @Override
//                public void run() {
//                    photoView.setVisibility(View.GONE);
//                    img.setVisibility(View.VISIBLE);
//                }
//            });
//        } else {
//            super.onBackPressed();
//        }
//    }
}
